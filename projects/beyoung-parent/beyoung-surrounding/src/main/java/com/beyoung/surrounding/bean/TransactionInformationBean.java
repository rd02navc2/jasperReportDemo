package com.beyoung.surrounding.bean;

public class TransactionInformationBean {
    private TransactionInformation transaction_information;

    public TransactionInformation getTransaction_information() {
        return transaction_information;
    }

    public void setTransaction_information(TransactionInformation transaction_information) {
        this.transaction_information = transaction_information;
    }


    public class TransactionInformation {


        private String transaction_id;
        private String mmrm_tid;

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getMmrm_tid() {
            return mmrm_tid;
        }

        public void setMmrm_tid(String mmrm_tid) {
            this.mmrm_tid = mmrm_tid;
        }
    }
}

