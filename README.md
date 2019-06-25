# AgentTNE 2019

This repository holds the research made, paper and presentation produced as well as the code for agent TNE19.

*TNE* stands for *Tecnologias para Negócio Eletrónico* which translates to *Electronic Business Technologies*. Thus the agent is named after the course in which this agent was developed and the year of participation in a PowerTAC competition simulation.

**Details of implementation and strategies used as well as results can be found in the form of a [scientific paper](docs/paper-tne19.pdf) and [presentation](docs/presentation-tne19.pdf).**


## How to run

In order to run both the server and the broker you should have maven installed.
To run the prediction model you need python3 and pip.

**Run first the server, then the prediction model, then each of the desired agents to compete in the simulation.**


### Running server
  1. `cd server-distribution-release-1.6.0`
  2. `mvn compile`
  3. `mvn -Pweb2`

### Running prediction model
  1. `pip install -r requirements.txt`
  2. `python3 predict.py`

### Launching broker
  1. `cd tne19-broker`
  2. `mvn compile`
  3. `mvn exec:exec`