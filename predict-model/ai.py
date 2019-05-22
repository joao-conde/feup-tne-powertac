import numpy as np
import pandas as pd
from sklearn import datasets, linear_model, model_selection, metrics, neural_network


# def create_predict_model(verbose):
#     data = pd.read_csv('data.csv')
#     np.set_printoptions(threshold=np.inf) # display entire array

#     attr_cols = ["Timeslot","WeekDay","CA24","CP24","T24","WS24","CA23","CP23","T23","WS23","CA22","CP22","T22","WS22","CA21","CP21","T21","WS21","CA20","CP20","T20","WS20","CA19","CP19","T19","WS19","CA18","CP18","T18","WS18","CA17","CP17","T17","WS17","CA16","CP16","T16","WS16","CA15","CP15","T15","WS15","CA14","CP14","T14","WS14","CA13","CP13","T13","WS13","CA12","CP12","T12","WS12","CA11","CP11","T11","WS11","CA10","CP10","T10","WS10","CA9","CP9","T9","WS9","CA8","CP8","T8","WS8","CA7","CP7","T7","WS7","CA6","CP6","T6","WS6","CA5","CP5","T5","WS5","CA4","CP4","T4","WS4","CA3","CP3","T3","WS3","CA2","CP2","T2","WS2","CA1","CP1","T1","WS1","CT","CWS","PCA1","PCP1","PCA2","PCP2","PCA3","PCP3","PCA4","PCP4","PCA5","PCP5","PCA6","PCP6","PCA7","PCP7","PCA8","PCP8","PCA9","PCP9","PCA10","PCP10","PCA11","PCP11","PCA12","PCP12","PCA13","PCP13","PCA14","PCP14","PCA15","PCP15","PCA16","PCP16","PCA17","PCP17","PCA18","PCP18","PCA19","PCP19","PCA20","PCP20","PCA21","PCP21","PCA22","PCP22","PCA23","PCP23","PCA24","PCP24","TF1","WSF1","TF2","WSF2","TF3","WSF3","TF4","WSF4","TF5","WSF5","TF6","WSF6","TF7","WSF7","TF8","WSF8","TF9","WSF9","TF10","WSF10","TF11","WSF11","TF12","WSF12","TF13","WSF13","TF14","WSF14","TF15","WSF15","TF16","WSF16","TF17","WSF17","TF18","WSF18","TF19","WSF19","TF20","WSF20","TF21","WSF21","TF22","WSF22","TF23","WSF23","TF24","WSF24"]
#     x = data[attr_cols].values
#     predict_cols = ["TCA1","TCP1","TCA2","TCP2","TCA3","TCP3","TCA4","TCP4","TCA5","TCP5","TCA6","TCP6","TCA7","TCP7","TCA8","TCP8","TCA9","TCP9","TCA10","TCP10","TCA11","TCP11","TCA12","TCP12","TCA13","TCP13","TCA14","TCP14","TCA15","TCP15","TCA16","TCP16","TCA17","TCP17","TCA18","TCP18","TCA19","TCP19","TCA20","TCP20","TCA21","TCP21","TCA22","TCP22","TCA23","TCP23","TCA24","TCP24"]
#     y = data[predict_cols].values
#     x_train, x_test, y_train, y_test = model_selection.train_test_split(
#         x, y, test_size=0.3)

#     regressor = neural_network.MLPRegressor()
#     regressor.fit(x_train, y_train)

#     if verbose:
#         y_pred = regressor.predict(x_test)
#         df = pd.DataFrame({'Actual': y_test.flatten(),
#                            'Predicted': y_pred.flatten()})
#         print(df)
#         print('Mean Absolute Error:', metrics.mean_absolute_error(y_test, y_pred))
#         print('Mean Squared Error:', metrics.mean_squared_error(y_test, y_pred))
#         print('Root Mean Squared Error:', np.sqrt(
#             metrics.mean_squared_error(y_test, y_pred)))

#     return regressor

def create_energy_predict_model(verbose):
    data = pd.read_csv('data.csv')
    np.set_printoptions(threshold=np.inf) # display entire array

    attr_cols = ["Timeslot","WeekDay","CA24","CP24","T24","WS24","CA23","CP23","T23","WS23","CA22","CP22","T22","WS22","CA21","CP21","T21","WS21","CA20","CP20","T20","WS20","CA19","CP19","T19","WS19","CA18","CP18","T18","WS18","CA17","CP17","T17","WS17","CA16","CP16","T16","WS16","CA15","CP15","T15","WS15","CA14","CP14","T14","WS14","CA13","CP13","T13","WS13","CA12","CP12","T12","WS12","CA11","CP11","T11","WS11","CA10","CP10","T10","WS10","CA9","CP9","T9","WS9","CA8","CP8","T8","WS8","CA7","CP7","T7","WS7","CA6","CP6","T6","WS6","CA5","CP5","T5","WS5","CA4","CP4","T4","WS4","CA3","CP3","T3","WS3","CA2","CP2","T2","WS2","CA1","CP1","T1","WS1","CT","CWS","PCA1","PCP1","PCA2","PCP2","PCA3","PCP3","PCA4","PCP4","PCA5","PCP5","PCA6","PCP6","PCA7","PCP7","PCA8","PCP8","PCA9","PCP9","PCA10","PCP10","PCA11","PCP11","PCA12","PCP12","PCA13","PCP13","PCA14","PCP14","PCA15","PCP15","PCA16","PCP16","PCA17","PCP17","PCA18","PCP18","PCA19","PCP19","PCA20","PCP20","PCA21","PCP21","PCA22","PCP22","PCA23","PCP23","PCA24","PCP24","TF1","WSF1","TF2","WSF2","TF3","WSF3","TF4","WSF4","TF5","WSF5","TF6","WSF6","TF7","WSF7","TF8","WSF8","TF9","WSF9","TF10","WSF10","TF11","WSF11","TF12","WSF12","TF13","WSF13","TF14","WSF14","TF15","WSF15","TF16","WSF16","TF17","WSF17","TF18","WSF18","TF19","WSF19","TF20","WSF20","TF21","WSF21","TF22","WSF22","TF23","WSF23","TF24","WSF24"]
    x = data[attr_cols].values
    predict_cols = ["TCA1","TCA2","TCA3","TCA4","TCA5","TCA6","TCA7","TCA8","TCA9","TCA10","TCA11","TCA12","TCA13","TCA14","TCA15","TCA16","TCA17","TCA18","TCA19","TCA20","TCA21","TCA22","TCA23","TCA24"]
    y = data[predict_cols].values
    x_train, x_test, y_train, y_test = model_selection.train_test_split(
        x, y, test_size=0.3)

    # regressor = neural_network.MLPRegressor()
    regressor = linear_model.LinearRegression()

    regressor.fit(x_train, y_train)

    if verbose:
        print("---ENERGY PREDICTION MODEL---")
        y_pred = regressor.predict(x_test)
        df = pd.DataFrame({'Actual': y_test.flatten(),
                           'Predicted': y_pred.flatten()})
        print(df)
        print('Mean Absolute Error:', metrics.mean_absolute_error(y_test, y_pred))
        print('Mean Squared Error:', metrics.mean_squared_error(y_test, y_pred))
        print('Root Mean Squared Error:', np.sqrt(
            metrics.mean_squared_error(y_test, y_pred)))

    return regressor

def create_price_predict_model(verbose):
    data = pd.read_csv('data.csv')
    np.set_printoptions(threshold=np.inf) # display entire array

    attr_cols = ["Timeslot","WeekDay","CA24","CP24","T24","WS24","CA23","CP23","T23","WS23","CA22","CP22","T22","WS22","CA21","CP21","T21","WS21","CA20","CP20","T20","WS20","CA19","CP19","T19","WS19","CA18","CP18","T18","WS18","CA17","CP17","T17","WS17","CA16","CP16","T16","WS16","CA15","CP15","T15","WS15","CA14","CP14","T14","WS14","CA13","CP13","T13","WS13","CA12","CP12","T12","WS12","CA11","CP11","T11","WS11","CA10","CP10","T10","WS10","CA9","CP9","T9","WS9","CA8","CP8","T8","WS8","CA7","CP7","T7","WS7","CA6","CP6","T6","WS6","CA5","CP5","T5","WS5","CA4","CP4","T4","WS4","CA3","CP3","T3","WS3","CA2","CP2","T2","WS2","CA1","CP1","T1","WS1","CT","CWS","PCA1","PCP1","PCA2","PCP2","PCA3","PCP3","PCA4","PCP4","PCA5","PCP5","PCA6","PCP6","PCA7","PCP7","PCA8","PCP8","PCA9","PCP9","PCA10","PCP10","PCA11","PCP11","PCA12","PCP12","PCA13","PCP13","PCA14","PCP14","PCA15","PCP15","PCA16","PCP16","PCA17","PCP17","PCA18","PCP18","PCA19","PCP19","PCA20","PCP20","PCA21","PCP21","PCA22","PCP22","PCA23","PCP23","PCA24","PCP24","TF1","WSF1","TF2","WSF2","TF3","WSF3","TF4","WSF4","TF5","WSF5","TF6","WSF6","TF7","WSF7","TF8","WSF8","TF9","WSF9","TF10","WSF10","TF11","WSF11","TF12","WSF12","TF13","WSF13","TF14","WSF14","TF15","WSF15","TF16","WSF16","TF17","WSF17","TF18","WSF18","TF19","WSF19","TF20","WSF20","TF21","WSF21","TF22","WSF22","TF23","WSF23","TF24","WSF24"]
    x = data[attr_cols].values
    predict_cols = ["TCP1","TCP2","TCP3","TCP4","TCP5","TCP6","TCP7","TCP8","TCP9","TCP10","TCP11","TCP12","TCP13","TCP14","TCP15","TCP16","TCP17","TCP18","TCP19","TCP20","TCP21","TCP22","TCP23","TCP24"]
    y = data[predict_cols].values
    x_train, x_test, y_train, y_test = model_selection.train_test_split(
        x, y, test_size=0.3)

    # regressor = neural_network.MLPRegressor()
    regressor = linear_model.LinearRegression()
    regressor.fit(x_train, y_train)

    if verbose:
        print("---PRICE PREDICTION MODEL---")
        y_pred = regressor.predict(x_test)
        df = pd.DataFrame({'Actual': y_test.flatten(),
                           'Predicted': y_pred.flatten()})
        print(df)
        print('Mean Absolute Error:', metrics.mean_absolute_error(y_test, y_pred))
        print('Mean Squared Error:', metrics.mean_squared_error(y_test, y_pred))
        print('Root Mean Squared Error:', np.sqrt(
            metrics.mean_squared_error(y_test, y_pred)))

    return regressor


def main():
    energy_model = create_energy_predict_model(verbose=True)
    price_model = create_price_predict_model(verbose=True)


if __name__ == "__main__":
    main()
