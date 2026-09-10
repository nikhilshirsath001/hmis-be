package com.suma.hmis_service.models.constants;

import java.util.Set;

public class ApiConstant {
    private ApiConstant() {
    }

    public static class Controller {
        private Controller() {
        }

        public static final String HMIS = "api/hmis";
        public static final String DOCUMENT = "api/document";
    }

    public static class Hmis {
        private Hmis() {
        }

        public static final String PATIENT = "/patient";
        public static final String CREATE_PATIENT = "/patient/create";


        public static final String CONTACT_PERSON = "/contact-person";


    }

    public static class Document {
        public static final long MAX_FILE_SIZE = 20 * 1024 * 1024L;
        public static final long MAX_REQUEST_SIZE = 100 * 1024 * 1024L;
        public static final String ROOT_FOLDER = "C:/hmis/storage/patient-documents";
        public static final Set<String> ALLOWED_TYPES =
                Set.of("application/pdf", "image/jpeg", "image/png");
    }

}
