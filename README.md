# API-Development
ACCORD has identified a set of 7 APIs that are required.

1. **Definitions** – Communicating Definitions from BSDD to the other components
    - Re-use the BSDD API https://app.swaggerhub.com/apis/buildingSMART/Dictionaries/v1

2. **Building Codes and Rules** – Communicating rules from GraphDB to other components
    - API Proposal [here](https://accord-project.github.io/API-Development/buildingcodesandrules.html)

3. **Information Services** – microservices accessing information from information services
    - The specific APIs needed to access the various information services

4. **Data** – retrieving data against which a compliance check is performed (semantic/non-semantic)
    -Future Insight Data API: https://acc-hub.clearly.app/documentation/openapi
    - Ontotext have also expressed an interest in developing a graphdb hdf5 connection
    - OpenCDE API https://github.com/buildingSMART/OpenCDE-API

5. **Management** – management of microservice orchestration
    - Netflix have some open source offerings https://github.com/Netflix/conductor and https://github.com/Netflix/eureka
    - Suggested by ONTO: https://airflow.apache.org/

6. **Results** – triggering and getting feedback/results from microservices
    - API Proposal [here](https://accord-project.github.io/API-Development/results.html)

7. **Reconciliation** - API by which to perform reconciliation over BSDD.
    -Utilise the W3C API: https://www.w3.org/community/reports/reconciliation/CG-FINAL-specs-0.2-20230410/

# ACCORD Developed APIs

ACCORD is thus developing two API specifications:
1.  The [Building Codes and Rules](https://accord-project.github.io/API-Development/buildingcodesandrules.html) API 
2.  The [Results](https://accord-project.github.io/API-Development/results.html) API
3.  The [Data](https://accord-project.github.io/API-Development/data.html) API