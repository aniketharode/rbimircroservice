package com.users.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.users.model.UsersModel;
import com.users.repository.UsersRepo;
import com.users.request.AlbumResponseModel;
import com.users.request.UsersRequest;

import antlr.collections.List;
import feign.FeignException;

@Service("usersService")
public class UsersService implements UserDetailsService {
	
	@Autowired
	private UsersRepo userrepo;
	
	@Autowired
	private ModelMapper modelmapper;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private RestTemplate resttemplate;
	
	@Autowired
	private AlbumServiceClient albumServiceClient;
	
	@Autowired
	private Environment env;
	
	org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Transactional
	public ResponseEntity<Object> addUsers(UsersRequest request) {
		
		request.setPassword(bcrypt.encode(request.getPassword()));
		
	UsersModel usermodel =	modelmapper.map(request, UsersModel.class);
		
		Map<String, Object> map = new HashMap<>();
		
		
		map.put("details",userrepo.save(usermodel));
		
		
		return new ResponseEntity<Object>(map, HttpStatus.OK);
		
		
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UsersModel user = userrepo.findByUsername(username);
		
//		System.out.println(user);
//		
//		GrantedAuthority grant = new SimpleGrantedAuthority(user.getRole());
//		
//		java.util.List<GrantedAuthority> grantAutherities = new ArrayList<GrantedAuthority>();
//		grantAutherities.add(grant);
		
		
		if(user==null)
			throw new UsernameNotFoundException("no user present");
		
		
		return new User(user.getUsername(), user.getPassword(),true, true, true, true, new ArrayList<GrantedAuthority>());
		
	}
	
	public UsersRequest getUserByUserName(String username) {
	
		UsersModel user = userrepo.findByUsername(username);
		
		if(user==null)
			throw new UsernameNotFoundException("no user present");
		
		
		return modelmapper.map(user, UsersRequest.class);
		
		
	}
	
	
	
	
	
	public UsersRequest getUserByUserId(Long userId) {
		
	Optional<UsersModel> users =	userrepo.findById(userId);
		
	UsersRequest userreq = null;
	  if(users.isPresent()) {
		  userreq = modelmapper.map(users.get(), UsersRequest.class);
	  }
	  
	  String albumurl = String.format(env.getProperty("album.url"),userId);
	  
	  
	
	//ResponseEntity<java.util.List<AlbumResponseModel>> albumres =  resttemplate.exchange(albumurl, HttpMethod.GET,null, new ParameterizedTypeReference<java.util.List<AlbumResponseModel>>() {
	//});
	  
	  log.info("before calling the album service");
	
	java.util.List<AlbumResponseModel> 
				list = albumServiceClient.getAlbumDetails(userId);
			
	  log.info("after calling the album service");
	
	userreq.setAlbum(list);
	
	return userreq;
	
	
		
	}
	
	
	
	

}
