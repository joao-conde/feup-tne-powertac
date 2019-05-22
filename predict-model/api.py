from flask import jsonify, request
from flask_api import FlaskAPI

import ai

app = FlaskAPI(__name__)


@app.route('/predict/energy', methods=['POST'])
def predict_energy():
    prediction = energy_model.predict(request.get_json()['data'])
    return jsonify({'prediction': prediction[0].tolist()})


@app.route('/predict/price', methods=['POST'])
def predict_price():
    prediction = price_model.predict(request.get_json()['data'])
    return jsonify({'prediction': prediction[0].tolist()})


if __name__ == "__main__":
    energy_model = ai.create_energy_predict_model(verbose=True)
    price_model = ai.create_price_predict_model(verbose=True)
    app.run(debug=True)
