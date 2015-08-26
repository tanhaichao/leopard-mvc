package io.leopard.web.passport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class PassportValidateImpl extends AbstractPassportValidate {

	@Override
	public PassportUser validate(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

}
