package org.grails.twitter

import grails.plugins.springsecurity.Secured

import org.grails.twitter.auth.Person

@Secured('IS_AUTHENTICATED_FULLY')
class StatusController {

	def springSecurityService
	def statusService
	
    def index = {				
		def messages = currentUserTimeline()
		[statusMessages: messages]
    }
	
	def updateStatus = {		
		def status = new Status(message: params.message)
		status.author = lookupPerson()
		status.save()		
        def messages = currentUserTimeline()
        render template: 'statusMessages', collection: messages, var: 'statusMessage'
	}

	def follow = {
		def per = Person.get(params.id)
		if(per) {
			def currentUser = lookupPerson()
			currentUser.addToFollowed(per)
			currentUser.save()			
		}
		redirect action: 'index'
	}
	
	private lookupPerson() {
		Person.get(springSecurityService.principal.id)
	}
		
	private currentUserTimeline() {		

		   def per = lookupPerson()
		   
		   def messages = Status.withCriteria {
				or {
					author {
						eq 'username', per.username
					}
					if (per.followed) {
						inList 'author', per.followed
					}
				}
				maxResults 10
				order 'dateCreated', 'desc'
			}
		messages
	}
	
}
