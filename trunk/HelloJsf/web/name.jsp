<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="style.css" rel="stylesheet" type="text/css" />
    <title>Greeting</title>
  </head>
  <body>
    <f:view>
      <h2><h:outputText value="#{msgs.header}" styleClass="pageHeader" /></h2>
      <h:form>
        <p>
          <h:commandLink value="English" action="#{localeChanger.englishAction}" immediate="true" /> |
          <h:commandLink value="Deutsch" action="#{localeChanger.germanAction}" immediate="true" /> |
          <h:commandLink value="Nederlands" action="#{localeChanger.dutchAction}" immediate="true" /> |
        </p>
        <p>
          <h:messages layout="table" styleClass="messages" />
        </p>
        <p>
          <h:outputText value="#{msgs.namePrompt}" styleClass="label" />:
          <h:inputText value="#{user.name}" styleClass="textField" required="true" />
        </p>
        <p>
          <h:commandButton id="submit" value="#{msgs.submitButton}" action="success" styleClass="button" />
        </p>
      </h:form>
    </f:view>
  </body>
</html>
