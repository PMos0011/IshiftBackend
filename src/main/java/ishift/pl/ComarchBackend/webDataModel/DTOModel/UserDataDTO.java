package ishift.pl.ComarchBackend.webDataModel.DTOModel;

public class UserDataDTO {
    private String newLogin;
    private String newPassword;
    private String oldLogin;
    private String oldPassword;

    public String getNewLogin() {
        return newLogin;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getOldLogin() {
        return oldLogin;
    }

    public String getOldPassword() {
        return oldPassword;
    }
}
