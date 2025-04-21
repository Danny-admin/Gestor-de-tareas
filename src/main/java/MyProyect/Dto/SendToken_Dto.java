package MyProyect.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SendToken_Dto {
    private String statusToken;

    public SendToken_Dto(String token) {
        this.statusToken = token;
    }
}
