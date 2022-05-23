package capstone.Runtogether.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class Response<T> {

    private int status;
    private String message;
    private T data;


    public Response(final int status, final String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public Response(final int status, final String message,final T t) {
        this.status = status;
        this.message = message;
        this.data = t;
    }


/*    public static <T> Response<T> res(final int status, final String message) {
        return res(status, message, null);
    }

    public static <T> Response<T> res(final int status, final String message, final T t) {
        return Response.<T>builder()
                .data(t)
                .status(status)
                .message(message)
                .build();
    }*/
}