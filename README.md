# feup-tne-powertac
Agent to compete in PowerTAC 2019

Tit-for-tat retail strat and balancing wholesale predicting mean average prices buying when lowest

# How to run

In order to run both the server and the broker you should have maven installed.

Server:
  - cd server-distribution-release-1.6.0
  - mvn -Pweb2

Broker:
  - cd tne19-broker
  - mvn compile
  - mvn exec:exec
