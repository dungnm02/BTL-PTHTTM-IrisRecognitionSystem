import cv2
import numpy as np


def min_bounding_circle(mask):
    """
      Nhận vùng ảnh.
      Trả về đường tròn ngoại tiếp nhỏ nhất của vùng ảnh.
    """
    contours, _ = cv2.findContours(
        mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    largest_contour = max(contours, key=cv2.contourArea)
    (x, y), radius = cv2.minEnclosingCircle(largest_contour)
    center = (int(x), int(y))
    radius = int(radius)
    return center, radius


def daugman_normalization(image, height, width, r_in, r_out):
    """
      Nhận ảnh, chiều cao và chiều ngang ngang ảnh đầu ra, bán kính đường tròn trong và ngoài.
      Trả về hình ảnh được chuẩn hóa theo daugman.
    """
    thetas = np.arange(0, 2 * np.pi, 2 * np.pi / width)  # Theta values
    r_out = r_in + r_out
    # Create empty flatten image
    flat = np.zeros((height, width, 3), np.uint8)
    circle_x = int(image.shape[0] / 2)
    circle_y = int(image.shape[1] / 2)
    for i in range(width):
        for j in range(height):
            theta = thetas[i]  # value of theta coordinate
            r_pro = j / height  # value of r coordinate(normalized)
            # get coordinate of boundaries
            Xi = circle_x + r_in * np.cos(theta)
            Yi = circle_y + r_in * np.sin(theta)
            Xo = circle_x + r_out * np.cos(theta)
            Yo = circle_y + r_out * np.sin(theta)
            # the matched cartesian coordinates for the polar coordinates
            Xc = (1 - r_pro) * Xi + r_pro * Xo
            Yc = (1 - r_pro) * Yi + r_pro * Yo
            color = image[int(Xc)][int(Yc)]  # color of the pixel
            flat[j][i] = color
    return flat


def crop(image, iris_rad):
    """
      input : image : numpy array
              iris_rad : int
      output : image : numpy array
    """
    image = cv2.resize(image, (640, 480), interpolation=cv2.INTER_LINEAR)
    image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    gray = cv2.medianBlur(image, 11)

    ret, _ = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
    circles = cv2.HoughCircles(gray, cv2.HOUGH_GRADIENT, 1, 50, param1=ret, param2=30, minRadius=20,
                               maxRadius=100)
    if circles is None:
        return None, None

    circles = circles[0, :, :]
    circles = np.int16(np.around(circles))

    for i in circles[:]:
        image = image[i[1] - i[2] - iris_rad:i[1] + i[2] +
                      iris_rad, i[0] - i[2] - iris_rad:i[0] + i[2] + iris_rad]
        radius = i[2]
    return image, radius


def recflection_remove(image):
    """
        input: image : numpy array
        output: result_image : numpy array
    """
    ret, mask = cv2.threshold(image, 150, 255, cv2.THRESH_BINARY)
    kernel = np.ones((5, 5), np.uint8)
    dilation = cv2.dilate(mask, kernel, iterations=1)
    result_image = cv2.inpaint(image, dilation, 5, cv2.INPAINT_TELEA)
    return result_image


def check(img_path):
    # Bài toán 1
    yolo_model_path = ""
    yolo_model = YOLO('yolo_model_path')
    # DỰ ĐOÁN
    # Đọc ảnh
    image = cv2.imread(img_path)
    # Dự đoán
    predict = yolo_model.predict(img_path)

    # CHUẨN HÓA ẢNH
    # Lấy đường tròn ngoại tiếp nhỏ nhất quanh mống mắt
    iris_mask = (predict[0].masks.data[0].cpu().numpy()*255).astype("uint8")
    _, iris_rad = min_bounding_circle(iris_mask)
    # Crop ảnh
    cropped_image, radius = crop(image, iris_rad)
    # cv2_imshow(cropped_image)
    # Xử lý phản chiếu
    cropped_image = recflection_remove(cropped_image)
    # cv2_imshow(cropped_image)
    # Chuẩn hóa Daugman
    normalized_image = daugman_normalization(
        cropped_image, 60, 360, radius, iris_rad)
    # cv2_imshow(flat_image)

    # CẢI THIỆN CHI TIẾT
    normalized_image = cv2.cvtColor(normalized_image, cv2.COLOR_BGR2GRAY)
    normalized_image = cv2.equalizeHist(normalized_image)
    cv2.imwrite("normalized_image.jpg", normalized_image)

    # Load model
    resnet_model_path = ""
    resnet_model = load_learner(resnet_model_path)
    predict = resnet_model.predict(normalized_image)
    # Trả về nhãn giống nhất
    return predict[0]
