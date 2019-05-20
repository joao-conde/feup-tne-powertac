from flask import jsonify, request
from flask_api import FlaskAPI

import ai

app = FlaskAPI(__name__)


@app.route('/predict', methods=['POST'])
def predict():
    prediction = model.predict(request.get_json()['data'])
    return jsonify({'prediction': prediction[0]})


if __name__ == "__main__":
    model = ai.create_predict_model(verbose=False)
    app.run(debug=True)
