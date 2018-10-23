package com.jun.baiduidentityauthapp.model;

/**
 * 身份证检测识别结果
 */
public class IDCardDetectionResponse {

    private long log_id;
    private int words_result_num;
    private int direction;
    private String image_status;
    private WordsResultBean words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getImage_status() {
        return image_status;
    }

    public void setImage_status(String image_status) {
        this.image_status = image_status;
    }

    public WordsResultBean getWords_result() {
        return words_result;
    }

    public void setWords_result(WordsResultBean words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {

        private 住址Bean 住址;
        private 出生Bean 出生;
        private 姓名Bean 姓名;
        private 公民身份号码Bean 公民身份号码;
        private 性别Bean 性别;
        private 民族Bean 民族;

        public 住址Bean get住址() {
            return 住址;
        }

        public void set住址(住址Bean 住址) {
            this.住址 = 住址;
        }

        public 出生Bean get出生() {
            return 出生;
        }

        public void set出生(出生Bean 出生) {
            this.出生 = 出生;
        }

        public 姓名Bean get姓名() {
            return 姓名;
        }

        public void set姓名(姓名Bean 姓名) {
            this.姓名 = 姓名;
        }

        public 公民身份号码Bean get公民身份号码() {
            return 公民身份号码;
        }

        public void set公民身份号码(公民身份号码Bean 公民身份号码) {
            this.公民身份号码 = 公民身份号码;
        }

        public 性别Bean get性别() {
            return 性别;
        }

        public void set性别(性别Bean 性别) {
            this.性别 = 性别;
        }

        public 民族Bean get民族() {
            return 民族;
        }

        public void set民族(民族Bean 民族) {
            this.民族 = 民族;
        }

        public static class 住址Bean {

            private LocationBean location;
            private String words;

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBean {

                private int width;
                private int top;
                private int height;
                private int left;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }
            }
        }

        public static class 出生Bean {

            private LocationBeanX location;
            private String words;

            public LocationBeanX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanX {

                private int width;
                private int top;
                private int height;
                private int left;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }
            }
        }

        public static class 姓名Bean {

            private LocationBeanXX location;
            private String words;

            public LocationBeanXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXX {

                private int width;
                private int top;
                private int height;
                private int left;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }
            }
        }

        public static class 公民身份号码Bean {

            private LocationBeanXXX location;
            private String words;

            public LocationBeanXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXX {

                private int width;
                private int top;
                private int height;
                private int left;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }
            }
        }

        public static class 性别Bean {

            private LocationBeanXXXX location;
            private String words;

            public LocationBeanXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXX {

                private int width;
                private int top;
                private int height;
                private int left;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }
            }
        }

        public static class 民族Bean {

            private LocationBeanXXXXX location;
            private String words;

            public LocationBeanXXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXXX {

                private int width;
                private int top;
                private int height;
                private int left;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }
            }
        }
    }
}