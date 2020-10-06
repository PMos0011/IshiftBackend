package ishift.pl.ComarchBackend.webDataModel.DTOModel;

import java.util.List;

public class UsersListDTO {
    private List<String> userList;

    public UsersListDTO(List<String> userList) {
        this.userList = userList;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
}
