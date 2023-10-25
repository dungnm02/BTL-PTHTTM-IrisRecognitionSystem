from ultralytics import YOLO
from fastai.vision.all import *


def train_and_eval(algo_type, dataset_path):
    """
        Train a model of this algo_type with this dataset. Return the path to the model.
        input : algo_type : string
                dataset_path : string
        output : model_path : string
    """
    model_path = ""
    TP = 0
    FP = 0
    FN = 0
    if algo_type == "seg":
        # For segmentation model
        model = YOLO('yolov8n-seg.pt')
        model.train(
            data=dataset_path + "\data.yaml",
            epochs=1,
            imgsz=640,
            device='mps',
        )
        model_path = get_newest_folder(
            "D:\\Projects\\BTL-PTHTTM-IrisRecognitionSystem\\runs\\segment") + "\\weights\\best.pt"
        # Evaluate
        metrics = model.val()
        confusion_matrix = metrics.confusion_matrix
        TP = int(sum(confusion_matrix.matrix.diagonal()[:-1]))
        FP = int(sum(confusion_matrix.matrix.sum(1)[:-1]) - TP)
        FN = int(sum(confusion_matrix.matrix.sum(0)[:-1]) - TP)

    elif algo_type == "clas":
        # For classification model
        data = ImageDataLoaders.from_folder(dataset_path)
        learn = cnn_learner(data, models.resnet34)
        learn.export('model.pkl')
        model_path = dataset_path + "\model.pkl"
        # Evaluate
        # Get the confusion matrix
        interp = ClassificationInterpretation.from_learner(learn)
        confusion_matrix = interp.confusion_matrix()
        num_label = len(confusion_matrix)  # Number of label
        TP = 0  # total TP
        FP = 0  # total FP
        FN = 0  # total FN
        for i in range(num_label):
            TP += confusion_matrix[i][i]
            FP += sum(confusion_matrix[i]) - confusion_matrix[i][i]
            FP += sum(confusion_matrix.T[i]) - confusion_matrix[i][i]
        # Calculate acc, pre, rec, f1
    if TP + FP + FN == 0:
        acc = 0
        pre = 0
        rec = 0
        f1 = 0
    else:
        acc = mdiv(TP, TP + FP + FN) * 100
        pre = mdiv(TP, TP + FP) * 100
        rec = mdiv(TP, TP + FN) * 100
        f1 = mdiv(2 * pre * rec, pre + rec)
    return model_path, round(acc, 2), round(pre, 2), round(rec, 2), round(f1, 2)


def mdiv(a, b):
    return a / b if b else 0


def get_newest_folder(directory):
    all_folders = [d for d in os.listdir(
        directory) if os.path.isdir(os.path.join(directory, d))]

    all_folders.sort(key=lambda folder: os.path.getctime(
        os.path.join(directory, folder)), reverse=True)

    newest_folder = all_folders[0]
    print(os.path.join(directory, newest_folder))
    return os.path.join(directory, newest_folder)
