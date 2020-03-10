package com.users.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.users.request.AlbumResponseModel;

import feign.FeignException;
import feign.Logger;
import feign.hystrix.FallbackFactory;

@FeignClient(name="albums-ws",fallbackFactory   = AlbumFallBackFactory.class)
public interface AlbumServiceClient {
	
	@GetMapping("/users/{id}/albums")
	List<AlbumResponseModel> getAlbumDetails(@PathVariable Long id);

}


@Component
class AlbumFallBackFactory implements FallbackFactory<AlbumServiceClient>{

	@Override
	public AlbumServiceClient create(Throwable cause) {
		// TODO Auto-generated method stub
		return new AlbumFallBack(cause);
	}
	
}

class AlbumFallBack implements AlbumServiceClient{
	
	private Throwable cause;
	
	org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
	
	public AlbumFallBack(Throwable thr) {
		this.cause=thr;
	}

	@Override
	public List<AlbumResponseModel> getAlbumDetails(Long id) {
		// TODO Auto-generated method stub
		
		
		if(cause instanceof FeignException || ((FeignException)cause).status()==404) {
			log.error("404 error is thrown "+cause.getLocalizedMessage());
		}
		else {
			log.error("other error is thrown "+cause.getLocalizedMessage());
		}
		
		
		return new ArrayList<AlbumResponseModel>();
	}
	
}