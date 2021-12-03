package com.gustavo.libraryapi.service;

import java.util.List;

public interface EmailSerivce {

	void sendMails(String message, List<String> mailsList);

}
