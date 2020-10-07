package ishift.pl.ComarchBackend.webDataModel.services;

import ishift.pl.ComarchBackend.webDataModel.repositiories.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDataRepository userDataRepository;

    @Autowired
    public UserDetailsServiceImpl(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //TODO orElse(null)
        return userDataRepository.findByUserName(s).orElse(null);
    }
}
