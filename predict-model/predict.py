import numpy as np
import pandas as pd
from sklearn import datasets, linear_model, model_selection, metrics


def main():
    data = pd.read_csv('data.csv')

    attr_cols = ["NoBrokers", "NoCustomers", "Consumption24hAgo", "Production24Ago", "TempForecast", "WindSpeedForecast", "WindDirectionForecast", "CloudsForecast", "ClearedQuantity24h", "Consumption-ProductionIn24h"]
    x = data[attr_cols].values
    y = data['Consumption'].values

    x_train, x_test, y_train, y_test = model_selection.train_test_split(x, y, test_size=0.2)

    regressor = linear_model.LinearRegression()  
    regressor.fit(x_train, y_train)

    y_pred = regressor.predict(x_test)

    df = pd.DataFrame({'Actual': y_test.flatten(), 'Predicted': y_pred.flatten()})
    print(df)
    print('Mean Absolute Error:', metrics.mean_absolute_error(y_test, y_pred))  
    print('Mean Squared Error:', metrics.mean_squared_error(y_test, y_pred))  
    print('Root Mean Squared Error:', np.sqrt(metrics.mean_squared_error(y_test, y_pred)))
  
    coeff_df = pd.DataFrame(regressor.coef_, attr_cols, columns=['Coefficient'])  
    print(coeff_df)

if __name__ == "__main__":
    main()