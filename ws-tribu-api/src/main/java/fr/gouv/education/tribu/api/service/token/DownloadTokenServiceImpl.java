package fr.gouv.education.tribu.api.service.token;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DownloadTokenServiceImpl implements DownloadTokenService {

	@Override
	@CachePut(value = { "tokenCache" }, key = "#tokenObject.token")
	public DownloadToken putInCache(DownloadToken tokenObject) {

		return tokenObject;
	}

	@Override
	@Cacheable(value = { "tokenCache" })
	public DownloadToken getInCache(String token) {

		// if token has expired, return a null value
		// otherwise, the cached object is returned by spring-cache

		return null;
	}

	@Override
	@CacheEvict(value = { "tokenCache" })
	public void removeFromCache(String token) {

	}
}
