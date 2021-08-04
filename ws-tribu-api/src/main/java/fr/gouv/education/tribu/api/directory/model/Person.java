package fr.gouv.education.tribu.api.directory.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Name;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;
import org.springframework.stereotype.Component;


/**
 * ODM of a person
 * 
 * @author Loïc Billon
 * @since 4.4
 */
@Component("person")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entry(objectClasses = {"portalPerson"})
public final class Person implements Serializable {

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;
    

    /** DN. */
    @Id
    private Name dn;

    /** CN. */
    @Attribute
    private String cn;

    /** SN. */
    @Attribute
    private String sn;

    /** Display name. */
    @Attribute
    private String displayName;

    /** Given name. */
    @Attribute
    private String givenName;

    /** Email. */
    @Attribute
    private String mail;

    /** Title. */
    @Attribute
    private String title;

    /** UID. */
    @Attribute
    private String uid;

    /** Profiles. */
    @Attribute(name = "portalPersonProfile")
    private List<Name> profiles;

    /** User password. */
    @Attribute
    @Transient
    private String userPassword;

//    /** Avatar */
//    @Transient
//    private Link avatar;

    /** External account indicator. */
    @Attribute(name = "portalPersonExternal")
    private Boolean external;

    /** Account validity date. */
    @Attribute(name = "portalPersonValidity")
    private Date validity;

    /** Creation date. */
    @Attribute(name = "portalPersonCreationDate")
    private Date creationDate;
    
    /** Last connection date. */
    @Attribute(name = "portalPersonLastConnection")
    private Date lastConnection;

    /** hash NumEN */
    @Attribute(name = "portalPersonHashNumen")
    private String hashNumen;
        
    /** Fonction */
    @Attribute(name = "portalPersonFonct")
    private String fonction;
    
    /** Fonction administrative */
    @Attribute(name = "portalPersonFonctAdm")
    private String fonctionAdm;
    
    /** RNE */
    @Attribute(name = "portalPersonRne")
    private String rne;
    
    /** RNEs d'exercice */
    @Attribute(name = "portalPersonRneExerc")
    private List<String> rneExerc;
    
    /** Responsables */
    @Attribute(name = "portalPersonRneResp")
    private List<String> rneResp;
    
    /** Nom académie */
    @Attribute(name = "portalPersonNomAca")
    private String nomAcademie;
    
    /** Code académie */
    @Attribute(name = "portalPersonCodeAca")
    private String codeAca;
    
    /** Mail académique */
    @Attribute(name = "portalPersonMailAca")
    private String mailAca;

    /**
     * Constructor.
     */
    public Person() {
        super();
        this.profiles = new ArrayList<Name>();
//        this.avatar = new Link(StringUtils.EMPTY, false);
    }


    /**
     * {@inheritDoc}
     */
    public Name getDn() {
        return this.dn;
    }


    /**
     * {@inheritDoc}
     */
    public void setDn(Name dn) {
        this.dn = dn;
    }


    /**
     * {@inheritDoc}
     */
    public String getCn() {
        return this.cn;
    }


    /**
     * {@inheritDoc}
     */
    public void setCn(String cn) {
        this.cn = cn;
    }


    /**
     * {@inheritDoc}
     */
    public String getSn() {
        return this.sn;
    }


    /**
     * {@inheritDoc}
     */
    public void setSn(String sn) {
        this.sn = sn;
    }


    /**
     * {@inheritDoc}
     */
    public String getDisplayName() {
        return this.displayName;
    }


    /**
     * {@inheritDoc}
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    /**
     * {@inheritDoc}
     */
    public String getGivenName() {
        return this.givenName;
    }


    /**
     * {@inheritDoc}
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }


    /**
     * {@inheritDoc}
     */
    public String getMail() {
        return this.mail;
    }


    /**
     * {@inheritDoc}
     */
    public void setMail(String mail) {
        this.mail = mail;
    }


    /**
     * {@inheritDoc}
     */
    public String getTitle() {
        return this.title;
    }


    /**
     * {@inheritDoc}
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * {@inheritDoc}
     */
    public String getUid() {
        return this.uid;
    }


    /**
     * {@inheritDoc}
     */
    public void setUid(String uid) {
        this.uid = uid;
    }


    /**
     * {@inheritDoc}
     */
    public List<Name> getProfiles() {
        return this.profiles;
    }


    /**
     * {@inheritDoc}
     */
    public void setProfiles(List<Name> profiles) {
        this.profiles = profiles;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean getExternal() {
        return this.external;
    }


    /**
     * {@inheritDoc}
     */
    public void setExternal(Boolean external) {
        this.external = external;
    }


    /**
     * {@inheritDoc}
     */
    public Date getValidity() {
        return this.validity;
    }


    /**
     * {@inheritDoc}
     */
    public void setValidity(Date validity) {
        this.validity = validity;
    }


	/**
     * {@inheritDoc}
     */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
     * {@inheritDoc}
     */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	/**
     * {@inheritDoc}
     */
    public Date getLastConnection() {
        return this.lastConnection;
    }


    /**
     * {@inheritDoc}
     */
    public void setLastConnection(Date lastConnection) {
        this.lastConnection = lastConnection;
    }


	public String getHashNumen() {
		return hashNumen;
	}


	public void setHashNumen(String hashNumen) {
		this.hashNumen = hashNumen;
	}

	
	public String getFonction() {
		return fonction;
	}


	public void setFonction(String fonction) {
		this.fonction = fonction;
	}


	public String getFonctionAdm() {
		return fonctionAdm;
	}


	public void setFonctionAdm(String fonctionAdm) {
		this.fonctionAdm = fonctionAdm;
	}


	public String getRne() {
		return rne;
	}


	public void setRne(String rne) {
		this.rne = rne;
	}


	public List<String> getRneExerc() {
		return rneExerc;
	}


	public void setRneExerc(List<String> rneExerc) {
		this.rneExerc = rneExerc;
	}

	
	public List<String> getRneResp() {
		return rneResp;
	}


	public void setRneResp(List<String> rneResp) {
		this.rneResp = rneResp;
	}


	public String getNomAcademie() {
		return nomAcademie;
	}


	public void setNomAcademie(String nomAcademie) {
		this.nomAcademie = nomAcademie;
	}


	public String getCodeAca() {
		return codeAca;
	}

	public void setCodeAca(String codeAca) {
		this.codeAca = codeAca;
	}


	public String getMailAca() {
		return mailAca;
	}


	public void setMailAca(String mailAca) {
		this.mailAca = mailAca;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.dn.toString();
    }

}
