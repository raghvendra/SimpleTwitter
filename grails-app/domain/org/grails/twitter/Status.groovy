package org.grails.twitter

import org.grails.twitter.auth.*

class Status {

	String message
	Person author
	Date dateCreated
	
    static constraints = {
    }
}
