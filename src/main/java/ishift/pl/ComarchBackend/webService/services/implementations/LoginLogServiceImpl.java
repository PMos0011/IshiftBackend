package ishift.pl.ComarchBackend.webService.services.implementations;

import ishift.pl.ComarchBackend.webDataModel.model.LoginLog;
import ishift.pl.ComarchBackend.webDataModel.repositiories.LoginLogRepository;
import ishift.pl.ComarchBackend.webService.services.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
@Service
public class LoginLogServiceImpl implements LoginLogService {

    private final LoginLogRepository loginLogRepository;

    @Autowired
    public LoginLogServiceImpl(LoginLogRepository loginLogRepository) {
        this.loginLogRepository = loginLogRepository;
    }

    @Override
    public void saveLoginEvent(String userName, HttpServletRequest request) {

        String ipAddress = request.getHeader("X-Forward-For");
        if(ipAddress== null){

            ipAddress = request.getRemoteAddr();
        }
        ipAddress = ipAddress.split(",")[0];

        LoginLog loginLog = new LoginLog(ipAddress, userName);

        loginLogRepository.save(loginLog);

    }
}
