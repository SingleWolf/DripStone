package com.walker.collect.news.newslist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.walker.collect.api.JuheBaseResponse;

import java.util.List;

public class NewsListBean extends JuheBaseResponse {
    @SerializedName("result")
    @Expose
    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        @SerializedName("stat")
        @Expose
        private String stat;
        @SerializedName("data")
        @Expose
        private List<DataBean> data;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            @SerializedName("uniquekey")
            @Expose
            private String uniquekey;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("category")
            @Expose
            private String category;
            @SerializedName("author_name")
            @Expose
            private String author_name;
            @SerializedName("url")
            @Expose
            private String url;
            @SerializedName("thumbnail_pic_s")
            @Expose
            private String thumbnail_pic_s;

            public String getUniquekey() {
                return uniquekey;
            }

            public void setUniquekey(String uniquekey) {
                this.uniquekey = uniquekey;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getThumbnail_pic_s() {
                return thumbnail_pic_s;
            }

            public void setThumbnail_pic_s(String thumbnail_pic_s) {
                this.thumbnail_pic_s = thumbnail_pic_s;
            }
        }
    }
}
