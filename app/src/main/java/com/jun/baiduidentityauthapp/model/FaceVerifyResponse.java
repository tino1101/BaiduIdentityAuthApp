package com.jun.baiduidentityauthapp.model;

import java.util.List;

/**
 * 脸部识别结果
 */
public class FaceVerifyResponse {
    private int error_code;
    private String error_msg;
    private long log_id;
    private long timestamp;
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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {

        private ThresholdsBean thresholds;
        private double face_liveness;
        private List<FaceListBean> face_list;

        public ThresholdsBean getThresholds() {
            return thresholds;
        }

        public void setThresholds(ThresholdsBean thresholds) {
            this.thresholds = thresholds;
        }

        public double getFace_liveness() {
            return face_liveness;
        }

        public void setFace_liveness(double face_liveness) {
            this.face_liveness = face_liveness;
        }

        public List<FaceListBean> getFace_list() {
            return face_list;
        }

        public void setFace_list(List<FaceListBean> face_list) {
            this.face_list = face_list;
        }

        public static class ThresholdsBean {

            private double frr_1e4;
            private double frr_1e3;
            private double frr_1e2;

            public double getFrr_1e4() {
                return frr_1e4;
            }

            public void setFrr_1e4(double frr_1e4) {
                this.frr_1e4 = frr_1e4;
            }

            public double getFrr_1e3() {
                return frr_1e3;
            }

            public void setFrr_1e3(double frr_1e3) {
                this.frr_1e3 = frr_1e3;
            }

            public double getFrr_1e2() {
                return frr_1e2;
            }

            public void setFrr_1e2(double frr_1e2) {
                this.frr_1e2 = frr_1e2;
            }
        }

        public static class FaceListBean {

            private String face_token;
            private LocationBean location;
            private double face_probability;
            private AngleBean angle;
            private LivenessBean liveness;

            public String getFace_token() {
                return face_token;
            }

            public void setFace_token(String face_token) {
                this.face_token = face_token;
            }

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public double getFace_probability() {
                return face_probability;
            }

            public void setFace_probability(double face_probability) {
                this.face_probability = face_probability;
            }

            public AngleBean getAngle() {
                return angle;
            }

            public void setAngle(AngleBean angle) {
                this.angle = angle;
            }

            public LivenessBean getLiveness() {
                return liveness;
            }

            public void setLiveness(LivenessBean liveness) {
                this.liveness = liveness;
            }

            public static class LocationBean {

                private double left;
                private double top;
                private double width;
                private double height;
                private double rotation;

                public double getLeft() {
                    return left;
                }

                public void setLeft(double left) {
                    this.left = left;
                }

                public double getTop() {
                    return top;
                }

                public void setTop(double top) {
                    this.top = top;
                }

                public double getWidth() {
                    return width;
                }

                public void setWidth(double width) {
                    this.width = width;
                }

                public double getHeight() {
                    return height;
                }

                public void setHeight(double height) {
                    this.height = height;
                }

                public double getRotation() {
                    return rotation;
                }

                public void setRotation(double rotation) {
                    this.rotation = rotation;
                }
            }

            public static class AngleBean {

                private double yaw;
                private double pitch;
                private double roll;

                public double getYaw() {
                    return yaw;
                }

                public void setYaw(double yaw) {
                    this.yaw = yaw;
                }

                public double getPitch() {
                    return pitch;
                }

                public void setPitch(double pitch) {
                    this.pitch = pitch;
                }

                public double getRoll() {
                    return roll;
                }

                public void setRoll(double roll) {
                    this.roll = roll;
                }
            }

            public static class LivenessBean {

                private double livemapscore;

                public double getLivemapscore() {
                    return livemapscore;
                }

                public void setLivemapscore(double livemapscore) {
                    this.livemapscore = livemapscore;
                }
            }
        }
    }
}