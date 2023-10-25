from flask import Flask, request
from flask_restful import Resource, Api

import model_ulti as mu

app = Flask(__name__)
api = Api(app)


class TrainModel(Resource):
    def post(self, algo_type):
        dataset_path = request.json['datasetPath']
        model_path, acc, pre, rec, f1 = mu.train_and_eval(
            algo_type, dataset_path)
        return {
            'modelPath': model_path,
            'acc': str(acc),
            'pre': str(pre),
            'rec': str(rec),
            'f1': str(f1),
        }


api.add_resource(TrainModel, '/train/<algo_type>')

if __name__ == '__main__':
    app.run()
