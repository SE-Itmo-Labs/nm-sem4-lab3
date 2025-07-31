package com.ssnagin.collectionmanager.config;

public class Config {
    public class Core {


        public static String VERSION = "1.3.0";


        public static final int RESPONSE_SENDER_PARALLELISM = 8;
        public static final int REQUEST_RECEIVER_THREADS = 4;
    }

    public class Networking {
        public static final int PORT = 22815;
        public static final int BUFFER_SIZE = 65535;
    }

    public class Commands {

        public static final int MAX_SHOWN_COLLECTION_ELEMENTS = 10;

    }
}
