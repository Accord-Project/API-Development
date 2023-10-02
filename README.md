# API-Development
We have initially developed a list of API that we must explore. Below we will collect existing work in this area and existing open standards

1. **Definitions** – Communicating Definitions from BSDD to the other components
    - Here we should re-use the BSDD API https://app.swaggerhub.com/apis/buildingSMART/Dictionaries/v1

3. **Rules** – Communicating rules from GraphDB to other components
    - API Developed by Tom Previously: https://d-com-network.github.io/DCOMDocumentation/resources/compliancedocumentserver/ or [here](compliancedocumentserver.json)
    - Future insight API:

4. **Information Services** – microservices accessing information from information services
    - We need to define what information services we need in the project


5. **Data** – retrieving data against which a compliance check is performed (semantic/non-semantic)
    -Future Insight Data API: https://acc-hub.clearly.app/documentation/openapi
    - Ontotext have also expressed an interest in developing a graphdb hdf5 connection
    - OpenCDE API https://github.com/buildingSMART/OpenCDE-API

7. **Management** – management of microservice orchestration
    - Netflix have some open source offerings https://github.com/Netflix/conductor and https://github.com/Netflix/eureka
    - Suggested by ONTO: https://airflow.apache.org/

9. **Reports/Feedback** – triggering and getting feedback from microservices
    - Tom previous work: https://d-com-network.github.io/DCOMDocumentation/resources/resultserver/ or the [json](resultservice.json) and https://d-com-network.github.io/DCOMDocumentation/resources/ruleengineinterface/ or the [json](ruleengineinterface.json) 
    - BCF API https://github.com/buildingSMART/BCF-API
