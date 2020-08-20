package com.gestvicole.gestionavicole.utils;

public class Enumeration {

    public enum ORDER_STATE {
        WAITING("En attente"),
        READY("Prête"),
        MADE("Faite");

        private final String desc;

        ORDER_STATE(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum STOCK_OUT_STATE {
        WAITING("En attente"),
        VALIDATE("Valider");

        private final String desc;

        STOCK_OUT_STATE(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum INVOICE_STATE {
        OPEN("Ouverte"),
        VALIDATE("Validée"),
        IN_PAYMENT("En paiement"),
        PAID("Soldée"),
        CANCEL("Annulée");

        private final String desc;

        INVOICE_STATE(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
    public enum METHOD_OF_PAYMENT {
        CHECK("Chèque"),
        TRANSFER("Virement"),
        CASH("Espèce");

        private final String desc;

        METHOD_OF_PAYMENT(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum PAYMENT_STATE {
        PENDING("En attente"),
        VALIDATE("Payée");

        private final String desc;

        PAYMENT_STATE(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
