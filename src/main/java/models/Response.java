package models;

import java.util.Date;

/**
 * Created by sandya on 22/03/17.
 */
public class Response {
    private Integer counter;

    public Response(Integer counter){
        this.counter = counter;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
}
