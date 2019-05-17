import numpy as np
import pandas as pd
from sklearn import datasets, linear_model, model_selection, metrics, neural_network


def main():
    data = pd.read_csv('data.csv')


    attr_cols = ["Timeslot","CAT","CA24","CA23","CA22","CA21","CA20","CA19","CA18","CA17","CA16","CA15","CA14","CA13","CA12","CA11","CA10","CA9","CA8","CA7","CA6","CA5","CA4","CA3","CA2","CA1","T24","WS24","T23","WS23","T22","WS22","T21","WS21","T20","WS20","T19","WS19","T18","WS18","T17","WS17","T16","WS16","T15","WS15","T14","WS14","T13","WS13","T12","WS12","T11","WS11","T10","WS10","T9","WS9","T8","WS8","T7","WS7","T6","WS6","T5","WS5","T4","WS4","T3","WS3","T2","WS2","T1","WS1","TF1","WSF1"]
    x = data[attr_cols].values
    y = data['CAN'].values

    x_train, x_test, y_train, y_test = model_selection.train_test_split(x, y, test_size=0.5)

    regressor = neural_network.MLPRegressor()
    regressor.fit(x_train, y_train)

    y_pred = regressor.predict(x_test)

    df = pd.DataFrame({'Actual': y_test.flatten(), 'Predicted': y_pred.flatten()})
    print(df)
    print('Mean Absolute Error:', metrics.mean_absolute_error(y_test, y_pred))  
    print('Mean Squared Error:', metrics.mean_squared_error(y_test, y_pred))  
    print('Root Mean Squared Error:', np.sqrt(metrics.mean_squared_error(y_test, y_pred)))
    
    # For linear regressions
    # coeff_df = pd.DataFrame(regressor.coef_, attrs, columns=['Coefficient'])  
    # print(coeff_df)

if __name__ == "__main__":
    main()