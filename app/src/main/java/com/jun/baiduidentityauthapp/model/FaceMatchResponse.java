package com.jun.baiduidentityauthapp.model;

import java.util.List;

/**
 * 脸部对比结果
 */
public class FaceMatchResponse {
    private int error_code;
    private String error_msg;
    private long log_id;
    private long timestamp;
    private long cached;
    private ResultBean result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCached() {
        return cached;
    }

    public void setCached(long cached) {
        this.cached = cached;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {

        private double score;
        private List<FaceListBean> face_list;

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public List<FaceListBean> getFace_list() {
            return face_list;
        }

        public void setFace_list(List<FaceListBean> face_list) {
            this.face_list = face_list;
        }

        public static class FaceListBean {

            private String face_token;

            public String getFace_token() {
                return face_token;
            }

            public void setFace_token(String face_token) {
                this.face_token = face_token;
            }
        }
    }
}