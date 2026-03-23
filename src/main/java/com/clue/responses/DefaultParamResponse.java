package com.clue.responses;

import com.clue.entities.BasicUser;

import static com.clue.utils.Constants.USER_TYPE_CLIENT;
import static com.clue.utils.Constants.USER_TYPE_PROFESSIONAL;

public class DefaultParamResponse extends BasicResponse{
    private int userType;

    public DefaultParamResponse (boolean success, Integer errorCode,
                                 BasicUser basicUser) {
        super(success, errorCode);
        if (basicUser instanceof com.clue.entities.ClientEntity) {
            this.userType = USER_TYPE_CLIENT;
        } else if (basicUser instanceof com.clue.entities.ProffesionalEntity) {
            this.userType = USER_TYPE_PROFESSIONAL;
        }
    }

    public int getUserType() {
        return userType;
    }
}
