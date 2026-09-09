package com.suma.hmis_service.models.constants;

public class ApiConstant {
    private ApiConstant() {
    }

    public static class Controller {
        private Controller() {
        }

        public static final String HMIS = "api/hmis";
    }

    public static class Hmis {
        private Hmis() {
        }

        public static final String PATIENT = "/patient";
        public static final String CREATE_PATIENT = "/patient/create";

    }

}
