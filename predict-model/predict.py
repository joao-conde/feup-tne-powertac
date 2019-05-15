import numpy as np
import pandas as pd
import matplotlib.pyplot as plt  
from sklearn import datasets, linear_model, model_selection, metrics, neural_network


def main():
    data = pd.read_csv('data.csv')

    x = data[["Timeslot", "NoBrokers", "NoCustomers", "Consumption", "Production", "Consumption24hAgo", "Production24Ago", "TempForecast", "WindSpeedForecast", "WindDirectionForecast", "CloudsForecast", "ClearedQuantity", "ClearedQuantity24h", "Imbalance"]].values
    y = data['Imbalance24hSign'].values

    x_train, x_test, y_train, y_test = model_selection.train_test_split(x, y, test_size=0.2)

    regressor = neural_network.MLPClassifier(solver='lbfgs', alpha=1e-5, hidden_layer_sizes=(5, 2), random_state=1)
    regressor.fit(x_train, y_train)

    y_pred = regressor.predict(x_test)

    df = pd.DataFrame({'Actual': y_test.flatten(), 'Predicted': y_pred.flatten()})
    print(df)
    print('Mean Absolute Error:', metrics.mean_absolute_error(y_test, y_pred))  
    print('Mean Squared Error:', metrics.mean_squared_error(y_test, y_pred))  
    print('Root Mean Squared Error:', np.sqrt(metrics.mean_squared_error(y_test, y_pred)))

if __name__ == "__main__":
    main()