package com.se.projectmanagement;

/**
 * Created by Roman on 07.01.2017.
 */

public class Problem {

        private String id;
        //private String title;
        private String task;
        private String user;
        private String text;
        private String state;

        public Problem(String id, String task, String user, String text, String state) {
            if (id == null)
                throw new NullPointerException("id must not be NULL");
            this.id = id;
            //this.title = title;
            this.task = task;
            this.user= user;
            this.text = text;
            this.state = state;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTo() {
            return task;
        }

        public void setTo(String to) {
            this.task = to;
        }

        public String getText() {
        return text;
    }

        public String getTask() {
            return task;
        }
    public String getUser () {
        return user;
    }

    public String getState () {
        return state;
    }

        /*public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
*/
        public String toString() {
            return   text ;
        }

}
