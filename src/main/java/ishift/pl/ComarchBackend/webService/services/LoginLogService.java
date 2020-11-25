package ishift.pl.ComarchBackend.webService.services;

import javax.servlet.http.HttpServletRequest;

public interface LoginLogService {

    void saveLoginEvent (String userName, HttpServletRequest request);
}
