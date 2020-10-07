package fr.gouv.education.tribu.api.service.token;

/**
 * Interface used to put or get token in cache
 */
public interface DownloadTokenService {

	void removeFromCache(String token);

	DownloadToken getInCache(String token);

	DownloadToken putInCache(DownloadToken tokenObject);

}