package com.incomm.wmp.gprServices.outbound.ingo.service;

import org.springframework.beans.BeanUtils;

//import com.incomm.imp.neo.outbound.ingomoney.util.AppCtxIngo;

import com.incomm.wmp.gprServices.outbound.ingo.responses.*;
import com.incomm.wmp.gprServices.outbound.ingo.requests.*;
import org.tempuri.*;
import org.datacontract.schemas._2004._07.cpi_clearecd_entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;
import org.tempuri.FindCustomerResponse;

import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Validated
@Service
public class IngoServiceClient {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static Map<Class, String> soapActionMap = new HashMap<Class, String>();
    private static ApplicationContext appCtx;

    @Autowired
    private WebServiceTemplate template;

    private static IngoServiceClient ingoServiceClientInstance;

    @Value("${ingo_ws_android_connectid}")
    private String androidConnectId;

    @Value("${ingo_ws_android_token}")
    private String androidToken;

    @Value("${ingo_ws_ios_connectid}")
    private String iosConnectId;

    @Value("${ingo_ws_ios_token}")
    private String iosToken;

  /*  public static IngoServiceClient getInstance() {
        appCtx = AppCtxIngo.getInstance().getApplicationContext();
        return appCtx.getBean(IngoServiceClient.class);
    }*/

    // Create a mapping of request type to soap action.  There are found inside the wsdl file.
    static {
        soapActionMap.put(AuthenticatePartner.class, "http://tempuri.org/IIngoSDKSupportAPIs/AuthenticatePartner");
        soapActionMap.put(FindCustomer.class, "http://tempuri.org/IIngoSDKSupportAPIs/FindCustomer");
        soapActionMap.put(AddOrUpdateCard.class, "http://tempuri.org/IIngoSDKSupportAPIs/AddOrUpdateCard");
        soapActionMap.put(GetRegisteredCards.class, "http://tempuri.org/IIngoSDKSupportAPIs/GetRegisteredCards");
        soapActionMap.put(EnrollCustomer.class, "http://tempuri.org/IIngoSDKSupportAPIs/EnrollCustomer");
        soapActionMap.put(AddOrUpdateTokenizedCard.class, "http://tempuri.org/IIngoSDKSupportAPIs/AddOrUpdateTokenizedCard");
        soapActionMap.put(DeleteCard.class, "http://tempuri.org/IIngoSDKSupportAPIs/DeleteCard");
        soapActionMap.put(AuthenticateOBO.class, "http://tempuri.org/IIngoSDKSupportAPIs/AuthenticateOBO");
        soapActionMap.put(AddSessionAttributes.class, "http://tempuri.org/IIngoSDKSupportAPIs/AddSessionAttributes");
    }

    /**
     * <h1>Authenticate Partner</h1>
     * <p>
     * Makes call to AuthenticatePartner
     * Response contains sessionId for additional calls
     * </p>
     * <h2>Parameters</h2>
     * <ul>
     * <li>None</li>
     * </ul>
     *
     * @param authenticatePartnerRequest
     * @return IngoAuthenticatePartnerResponse
     */
    public IngoAuthenticatePartnerResponse authenticatePartner(@Valid IngoAuthenticatePartnerRequest authenticatePartnerRequest) {

        logger.info(authenticatePartnerRequest.toString());
        AuthenticatePartner request = new AuthenticatePartner();
        if (authenticatePartnerRequest.getPlatformType().equals("ANDROID")) {
            request.setPartnerConnectId(androidConnectId);
            request.setPartnerConnectToken(androidToken);
        } else if (authenticatePartnerRequest.getPlatformType().equals("IOS")) {
            request.setPartnerConnectId(iosConnectId);
            request.setPartnerConnectToken(iosToken);
        }
        AuthenticatePartnerResponse response = (AuthenticatePartnerResponse) template.marshalSendAndReceive(request, getSoapActionCallBack(request, authenticatePartnerRequest.getDeviceID(), authenticatePartnerRequest.getSessionID()));
        IngoAuthenticatePartnerResponse authenticatePartnerResponse_imp = new IngoAuthenticatePartnerResponse();
        String sessionId = response.getAuthenticatePartnerResult().getSessionId();

        if (sessionId != null && !sessionId.isEmpty()) {
            authenticatePartnerResponse_imp.setSessionID(sessionId);
        } else {
            authenticatePartnerResponse_imp.setErrorCode(response.getAuthenticatePartnerResult().getErrorCode().value());
            authenticatePartnerResponse_imp.setErrorMessage(response.getAuthenticatePartnerResult().getErrorMessage());
        }
        return authenticatePartnerResponse_imp;

    }

    /**
     * <h1>FindCustomer</h1>
     * <p>
     * Method is used to find a customer in the Ingo System. If a customer is not found, then
     * CustomerId will be an empty string.
     * </p>
     * <p>
     * <h2>Parameters</h2>
     * <ul>
     * <li>ssn - Required </li>
     * <li>dateOfBirth - Required </li>
     * </ul>
     * <h2>Sample Request/Response</h2>
     * <p>
     * Success or â€œCustomer Foundâ€ {â€œErrorCodeâ€:0,â€ErrorMessageâ€:â€â€œ,â€CustomerIdâ€:â€5cd26e67-66b1-4f12-b9f4-963ce27a03acâ€}
     * Failure or â€œCustomer Not Foundâ€ {â€œErrorCodeâ€:11000,â€ErrorMessageâ€:â€ The customer for the transaction is not foundâ€œ,â€CustomerIdâ€:â€â€}
     * </p>
     *
     * @param ingoFindCustomerRequest
     * @return IngoFindCustomerResponse
     */
    public IngoFindCustomerResponse findCustomer(@Valid IngoFindCustomerRequest ingoFindCustomerRequest) {

        FindCustomer request = new FindCustomer();
        BeanUtils.copyProperties(ingoFindCustomerRequest, request);

        FindCustomerResponse findCustomerResponse = (FindCustomerResponse) template.marshalSendAndReceive(request, getSoapActionCallBack(request, ingoFindCustomerRequest.getDeviceID(), ingoFindCustomerRequest.getSessionID()));

        IngoFindCustomerResponse ingoFindCustomerResponse = new IngoFindCustomerResponse();
        String customerId = findCustomerResponse.getFindCustomerResult().getCustomerId();

        if (customerId != null) {
            ingoFindCustomerResponse.setCustomerId(customerId);
        } else {
            ingoFindCustomerResponse.setErrorMessage(findCustomerResponse.getFindCustomerResult().getErrorMessage());
            ingoFindCustomerResponse.setErrorCode(findCustomerResponse.getFindCustomerResult().getErrorCode().value());
        }
        return ingoFindCustomerResponse;
    }

    /**
     * <h1>Add or Update Card</h1>
     * <p>This method is used to add or update a card within the system. This allows partners to make changes to cards without keeping track of which updates have been sent to Ingo. Partners may only update their own cards.
     * If the passed card data exactly matches the information in the Ingo system, no changes will be made, and the card wonâ€™t be reauthorized (AVS check, etc.). If any of the card data changes (e.g., expiration, etc.), the card will be reauthorized. An authorization failure will cause that card to be flagged as invalid in the system. In this case, a corresponding ErrorCode and ErrorMessage are returned.</p>
     * <p>
     * <h2>Parameters</h2>
     * <ul><
     * <li></li>customerId â€“ Required â€“ Customer whose card is being added or updated.
     * <li></li>cardNumber â€“ Required - Unformatted 15 or 16 digit card number.
     * ï‚· expirationMonthYear â€“ Required â€“ Unformatted month/year of the card. Should always be four digits. Months that are single digits should be preceded by a 0. For example, May 2015 would be represented as 0515.</li>
     * <li>cardNickname â€“ Required â€“ Nickname the user would like for the card. Something like â€œMy Allowanceâ€. Max Length is 50 characters.</li>
     * <li>nameOnCard â€“ Required â€“ the name of the user found on the front of the card.</li>
     * <li>addressLine1 â€“ Required â€“ the address used to register the card. Note, in Ingo, we require this to be the userâ€™s
     * registration address. Max length is 100 characters.</li>
     * <li>addressLine2 â€“ Optional â€“ the address used to register the card. Note, in Ingo, we require this to be the userâ€™s registration address. Max length is 100 characters. </li>
     * <li>city â€“ Required â€“ the city used to register the card. Note, in Ingo, we require this to be the userâ€™s registration city. Max Length is 50 characters. </li>
     * <li>state â€“ Required â€“ the state used to register the card. Note, in Ingo, we require this to be the userâ€™s registration state. Max Length is 2 characters. </li>
     * <li>zip â€“ Required â€“ the 5 digit zip code, no formatting, used to register the card. Note, in Ingo, we require this to be the userâ€™s registration zip. Max length is 5 characters. </li>
     * <p>JSON Request
     * {â€œcustomerIdâ€: â€œXYZ , "cardNumber": "4111111504856777", "expirationMonthYear": "1215", "cardNickname": "Home", "nameOnCard": "", "addressLine1": "", "addressLine2": "", "city": "", "state": "", "zip" : "37067"}</p>
     * <p>
     * JSON Response
     * {"ErrorCode":0,"ErrorMessage":"","Cards":[{"CardId":"42f76930-2662-4839-bf9f-8bfbe9b58cca", "CardNickname":"", "CardProgram":"", "CustomerId":"", "LastFourDigits":"6777"]}</p>
     */
    public IngoAddOrUpdateCardResponse makeIngoAddorUpdateCardCall(@Valid IngoAddOrUpdateCardRequest ingoAddOrUpdateCardRequest) {

        System.out.println("Method Called");
        AddOrUpdateCard request = new AddOrUpdateCard();
        BeanUtils.copyProperties(ingoAddOrUpdateCardRequest, request);

        System.out.println("About to make a call to Ingo outbound");
        AddOrUpdateCardResponse response = (AddOrUpdateCardResponse) template.marshalSendAndReceive(request, getSoapActionCallBack(request, ingoAddOrUpdateCardRequest.getDeviceID(), ingoAddOrUpdateCardRequest.getSessionID()));

        System.out.println("received response from IngoOutbound");

        IngoAddOrUpdateCardResponse ingoAddOrUpdateCardResponse = new IngoAddOrUpdateCardResponse();

        List<CardResponse> cardResponses = new ArrayList<CardResponse>();
        ArrayOfCard arrayOfCards = response.getAddOrUpdateCardResult().getCards();
        //List<Card> cards = arrayOfCards.getCard();//*(List<Card>) response.getAddOrUpdateCardResult().getCards();*//*

        if (arrayOfCards != null) {
            List<Card> cards = arrayOfCards.getCard();
            cards.forEach(cardList -> {
                CardResponse cardResponse = new CardResponse();
                BeanUtils.copyProperties(cardList, cardResponse);
                cardResponses.add(cardResponse);
            });


            ingoAddOrUpdateCardResponse.setCardList(cardResponses);

        } else {
            ingoAddOrUpdateCardResponse.setErrorCode(response.getAddOrUpdateCardResult().getErrorCode().value());
            ingoAddOrUpdateCardResponse.setErrorMessage(response.getAddOrUpdateCardResult().getErrorMessage());
        }

        return ingoAddOrUpdateCardResponse;

    }


    /**
     * <h1>Get Registered Cards</h1>
     * <p>
     * Returns a list of registered cards to the user. No PAN information is returned as part of this call.
     * </p>
     * <h2>Parameters</h2>
     * <ul>
     * <li> customerId â€“ Required â€“ The customer Id of the cards to be retrieved</li>
     * </ul>
     * <p>
     * <H2>Sample Requests</H2>
     * <p>JSON Request {â€œcustomerIdâ€:â€4g623rb9-48gh-7594-a5d6-268d37362d65â€}</p>
     * <p>JSON Response
     * {â€œerrorCodeâ€:0, â€œerrorMessageâ€:â€â€œ, â€œcardsâ€:[{â€œcardArtAsBase64Pngâ€:â€â€œ, â€œcardIdâ€:â€07ae09f7-8b55-4f4e-8f3b- eb5aadf6e398â€, â€œcardNicknameâ€:â€Nickname 2749â€, â€œcardProgramâ€:â€Super Duper Cardâ€, â€œcustomerIdâ€:â€5f73fb4e- 3e5a-428c-95cf-e4c6e4cbae14â€, â€œlastFourDigitsâ€:â€2749â€, â€œshowTermsAndConditionsâ€:true, â€œtermsAndConditionsIdâ€:â€TC1.1â€, â€œissuerTypeâ€:1, â€œexpirationMonthYearâ€:â€1115â€, â€œcardProgramPhoneâ€:â€5554445565â€, â€œcurrentCardMinâ€: 500, â€œcurrentCardMaxâ€: 50000 }]}</p>
     *
     * @param ingoGetRegisteredCardsRequest
     * @return IngoGetRegisteredCardsResponse
     */
    public IngoGetRegisteredCardsResponse getRegisteredCards(@Valid IngoGetRegisteredCardsRequest ingoGetRegisteredCardsRequest) {

        GetRegisteredCards request = new GetRegisteredCards();
        BeanUtils.copyProperties(ingoGetRegisteredCardsRequest, request);

        GetRegisteredCardsResponse response = (GetRegisteredCardsResponse) template.marshalSendAndReceive(request, getSoapActionCallBack(request, ingoGetRegisteredCardsRequest.getDeviceID(), ingoGetRegisteredCardsRequest.getSessionID()));

        IngoGetRegisteredCardsResponse ingoGetRegisteredCardsResponse = new IngoGetRegisteredCardsResponse();
        List<CardResponse> cardResponses = new ArrayList<>();
        ArrayOfCard arrayOfCards = response.getGetRegisteredCardsResult().getCards();

        if (response.getGetRegisteredCardsResult().getErrorCode().value().equals(ErrorCode.NONE.value())) {
            List<Card> cards = arrayOfCards.getCard();
            cards.forEach(cardList -> {
                CardResponse cardResponse = new CardResponse();
                BeanUtils.copyProperties(cardList, cardResponse);
                cardResponses.add(cardResponse);
            });
            ingoGetRegisteredCardsResponse.setCardList(cardResponses);
        } else {
            ingoGetRegisteredCardsResponse.setErrorCode(response.getGetRegisteredCardsResult().getErrorCode().value());
            ingoGetRegisteredCardsResponse.setErrorMessage(response.getGetRegisteredCardsResult().getErrorMessage());
        }

        return ingoGetRegisteredCardsResponse;
    }

    /**
     * <h1>EnrollCustomer</h1>
     * <p>This method is used to enroll a new customer. Before enrolling the customer, this method will check to see if either the SSN and/or email address are currently in use in the system. If the user data already exists, a corresponding ErrorCode and ErrorMessage are returned.</p>
     * <h2>Parameters</h2>
     * <ul>
     * <li>email â€“ Required - Email address of the customer. Must be a valid email address and contain an @ sign. </li>
     * <li>ssn â€“ Required - Social Security number of the user. Must be a 9 digit string, no spaces, no dashes. </li>
     * <li>firstName â€“ Required - First name of the person being added. Max length is 40 characters.</li>
     * <li>lastName â€“ Required â€“ Last name of the person being added. Max length is 50 characters.</li>
     * <li>middleInitial â€“ Optional â€“ single character middle initial. Max length is 1 character.</li>
     * <li>title â€“ Optional â€“ Title of the individual being added. Max length is 10 characters. Typically would contain one of Mrs,
     * Mr, etc.</li>
     * <li>suffix â€“ Optional â€“ Suffix of the individual being added. Max length is 10 characters. Typically would contain one of Jr, Sr, etc.</li>
     * <li>mobileNumber â€“ Required â€“ string containing the 10 digit mobile number of the user. Must be exactly 10 digits and contain no formatting characters.</li>
     * <li>homeNumber â€“ Optional â€“ string containing the 10 digit home number of the user. When present must be exactly 10 digits and contain no formatting characters.</li>
     * <li>addressLine1 â€“ Required â€“ string containing the first line of the customerâ€™s address. Max length is 100 characters.</li>
     * <li>addressLine2 â€“ Optional â€“ string containing the second line of the customerâ€™s address. Max length is 100 characters.</li>
     * <li>city â€“ Required â€“ string containing the city of the user. Max length is 50 characters.</li>
     * <li>state â€“ Required â€“ abbreviated state code. Must be two characters and follow the US state codes.</li>
     * <li>zip â€“ Required â€“ string containing the zip code of the user. Five digit zip is all that is required.</li>
     * <li>dateOfBirth â€“ Required - string containing the customerâ€™s date of birth in the format yyyy-MM-dd (e.g., 1978-09-18)</li>
     * <li>countryOfOrigin â€“ Optional â€“ optional string containing the userâ€™s country of origin for optional OFAC checking. This is not currently used or persisted.</li>
     * <li>Gender â€“ Optional â€“ single character string indicating M/F.</li>
     * <li>allowTexts â€“ Required â€“ Boolean indicating if the user is willing to accept text messages. The system does not
     * currently communicate via text at this time.</li>
     * <ul>
     * <p>
     * <h2>Sample Request/Response</h2>
     * <p>
     * JSON Request
     * {â€œemailâ€: â€œ5QY8A9Pr7b@gmail.comâ€, â€œssnâ€: â€œ684832266â€, â€œfirstNameâ€: â€œJasonâ€, â€œlastNameâ€: â€œUserâ€, â€œmiddleInitialâ€: â€œLâ€, â€œtitleâ€: â€œMr.â€, â€œsuffixâ€: â€œâ€œ, â€œmobileNumberâ€: â€œ6153456789â€, â€œhomeNumberâ€: â€œ6153456565â€, â€œaddressLine1â€: â€œ660 Bakers Bridge Laneâ€, â€œaddressLine2â€: â€œSuite 100â€, â€œcityâ€: â€œFranklinâ€, â€œstateâ€: â€œTNâ€, â€œzipâ€: â€œ37067â€, â€œdateOfBirthâ€: â€œ1978-09-11â€, â€œallowTextsâ€ : true }
     * JSON Response
     * {â€œErrorCodeâ€:0,â€ErrorMessageâ€:â€â€œ,â€CustomerIdâ€:â€333554bf-b273-4d6e-b4f8-d319a1929608}
     * </p>
     *
     * @param ingoEnrollCustomer
     * @return IngoEnrollCustomerResponse
     */
    public IngoEnrollCustomerResponse enrollCustomer(@Valid IngoEnrollCustomer ingoEnrollCustomer) {

        EnrollCustomer request = new EnrollCustomer();
        BeanUtils.copyProperties(ingoEnrollCustomer, request);

        EnrollCustomerResponse response = (EnrollCustomerResponse) template.marshalSendAndReceive(request, getSoapActionCallBack(request, ingoEnrollCustomer.getDeviceID(), ingoEnrollCustomer.getSessionID()));

        IngoEnrollCustomerResponse ingoEnrollCustomerResponse = new IngoEnrollCustomerResponse();

        String customerId = response.getEnrollCustomerResult().getCustomerId();

        if (customerId != null) {
            ingoEnrollCustomerResponse.setCustomerId(response.getEnrollCustomerResult().getCustomerId());
        } else {
            ingoEnrollCustomerResponse.setErrorCode(response.getEnrollCustomerResult().getErrorCode().value());
            ingoEnrollCustomerResponse.setErrorMessage(response.getEnrollCustomerResult().getErrorMessage());
        }
        return ingoEnrollCustomerResponse;
    }


    /**
     * <h1>AddOrUpdateTokenizedCard</h1>
     * <p>
     * Adds or updates an existing card within the system utilizing a alphanumeric value in place of sending the actual card PAN. This allows partners to make changes to cards without keeping track of which updates have been sent to Ingo. Partners may only update their own cards.
     * If the data is not modified, no changes are done, and the card wonâ€™t be validated. If the card number or expiration changes, the card will be reauthorized. A failure in authorization will mark that card as invalid in the system. This information is returned in the ErrorCode and ErrorMessage. Validation is only done if an appropriate validator is setup for the card program, which is on a card-by-card basis. Some partners may choose to not do validation.
     * In order to utilize the AddOrUpdateTokenized API method, it is required that Ingo has a direct method of sending load requests with your servers. This is due to only you knowing the actual card numbers being linked to the Tokenized placeholder value, which allows us not to use the normal OCT loading scenarios.
     * </p>
     * <h2>Parameters</h2>
     * <ul>
     * <li>customerId â€“ Required â€“ Customer whose card is being added or updated.</li>
     * ï‚· <li>cardToken â€“ Required - Up to 256 character long encoded card token.</li>
     * ï‚· <li>cardBin â€“ Required â€“ 6-8 digit bin number for card as string.</li>
     * ï‚· <li>last4OfCard â€“ Optional â€“ 4 digits that represent the card to the customer, typically the last 4 digits, as a string</li>
     * ï‚· <li>expirationMonthYear â€“ Optional â€“ Unformatted month/year of the card. Should always be four digits. Months that are single digits should be preceded by a 0. For example, May 2015 would be represented as 0515.</li>
     * ï‚· cardNickname â€“ Required â€“ Nickname the user would like for the card. Something like â€œMy Allowanceâ€. Max Length is 50 characters.</li>
     * ï‚· <li>nameOnCard â€“ Optional â€“ the name of the user found on the front of the card. </li>
     * ï‚· <li>addressLine1 â€“ Optional â€“ the address used to register the card. Note, in Ingo, we require this to be the userâ€™s
     * registration address. Max length is 100 characters.</li>
     * ï‚· <li>addressLine2 â€“ Optional â€“ the address used to register the card. Note, in Ingo, we require this to be the userâ€™s registration address. Max length is 100 characters.</li>
     * ï‚· <li>city â€“ Optional â€“ the city used to register the card. Note, in Ingo, we require this to be the userâ€™s registration city. Max Length is 50 characters.</li>
     * ï‚· <li>state â€“ Optional â€“ the state used to register the card. Note, in Ingo, we require this to be the userâ€™s registration state. Max Length is 2 characters.</li>
     * ï‚· <li>zip â€“ Optional â€“ the 5 digit zip code, no formatting, used to register the card. Note, in Ingo, we require this to be the userâ€™s registration zip. Max length is 5 characters.</li>
     * </ul>
     * <p>
     * <h2>Sample Request/Response</h2>
     * <p>
     * JSON Request
     * {â€œcustomerIdâ€: â€œXYZ , "cardToken": "42f76930-2662-4839-bf9f-8bfbe9b58cca ",â€cardBinâ€:â€12345â€, "expirationMonthYear": "1215", "cardNickname": "Home", "nameOnCard": "", "addressLine1": "", "addressLine2": "", "city": "", "state": "", "zip" : "37067"}
     * JSON Response
     * {"ErrorCode":0,"ErrorMessage":"","Cards":[{"cardId":" 3b2ef6d2-76f2-43bb-b9ab-dc5be2c5c3db", "cardNickname":"", "cardProgram":"", "customerId":"", "lastFourDigits":"****",â€hashIdâ€:â€â€]}
     * </p>
     *
     * @param ingoAddOrUpdateTokenizedCardRequest
     * @return
     */
    public IngoAddOrUpdateTokenizedCardResponse addOrUpdateTokenizedCard(@Valid IngoAddOrUpdateTokenizedCardRequest ingoAddOrUpdateTokenizedCardRequest) {

        AddOrUpdateTokenizedCard request = new AddOrUpdateTokenizedCard();
        BeanUtils.copyProperties(ingoAddOrUpdateTokenizedCardRequest, request);

        AddOrUpdateTokenizedCardResponse response = (AddOrUpdateTokenizedCardResponse) template.marshalSendAndReceive(request, getSoapActionCallBack(request, ingoAddOrUpdateTokenizedCardRequest.getDeviceID(), ingoAddOrUpdateTokenizedCardRequest.getSessionID()));

        IngoAddOrUpdateTokenizedCardResponse ingoAddOrUpdateTokenizedCardResponse = new IngoAddOrUpdateTokenizedCardResponse();

        final List<CardResponse> cardResponses = new ArrayList<CardResponse>();
        ArrayOfCard arrayOfCards = response.getAddOrUpdateTokenizedCardResult().getCards();

        if (!response.getAddOrUpdateTokenizedCardResult().getErrorCode().value().equals(ErrorCode.NONE.value())
                || !response.getAddOrUpdateTokenizedCardResult().getErrorCode().value().equals(ErrorCode.NONE.name())) {
            List<Card> cards = arrayOfCards.getCard();
            cards.forEach(cardList -> {
                CardResponse cardResponse = new CardResponse();
                BeanUtils.copyProperties(cardList, cardResponse);
                cardResponses.add(cardResponse);
            });

            ingoAddOrUpdateTokenizedCardResponse.setCardList(cardResponses);

        } else {
            ingoAddOrUpdateTokenizedCardResponse.setErrorCode(response.getAddOrUpdateTokenizedCardResult().getErrorCode().value());
            ingoAddOrUpdateTokenizedCardResponse.setErrorMessage(response.getAddOrUpdateTokenizedCardResult().getErrorMessage());
        }

        return ingoAddOrUpdateTokenizedCardResponse;
    }


    /**
     * <h1>Delete Card</h1>
     * <p>
     * Delete a given card. Note, the card can be added back, but will result in a different card entry in the database.
     * </p>
     * <h2>Parameters</h2>
     * <p>
     * <ul>
     * <li>customerId - Required</li>
     * <li>cardId - Required</li>
     * </ul>
     * <p>
     * <h2>Sample Request/Response</h2>
     * <p>
     * JSON Request
     * {â€œcustomerIdâ€:â€4g623rb9-48gh-7594-a5d6-268d37362d65â€ , â€œcardIdâ€:â€3b242db9-67fc-4087-b0d6-156d37362a13â€}
     * JSON Response
     * {â€œerrorCodeâ€:0, â€œerrorMessageâ€:â€â€œ}
     * </p>
     *
     * @param ingoDeleteCardRequest
     * @return DeleteCardResponse
     */
    public BaseResponse deleteCard(@Valid IngoDeleteCardRequest ingoDeleteCardRequest) {

        DeleteCard request = new DeleteCard();
        BeanUtils.copyProperties(ingoDeleteCardRequest, request);

        DeleteCardResponse deleteCardResponse = (DeleteCardResponse) template.marshalSendAndReceive(request, getSoapActionCallBack(request, ingoDeleteCardRequest.getDeviceID(), ingoDeleteCardRequest.getSessionID()));

        BaseResponse baseResponse = new BaseResponse();
        if(!deleteCardResponse.getDeleteCardResult().getErrorCode().value().equals(ErrorCode.NONE.value())) {
            baseResponse.setErrorCode(deleteCardResponse.getDeleteCardResult().getErrorCode().value());
            baseResponse.setErrorMessage(deleteCardResponse.getDeleteCardResult().getErrorMessage());
        }

        return baseResponse;
    }

    /**
     * <h1>AuthenticateOBO</h1>
     * <p>This method is used to authenticate a specific customer. If authentication is successful, a one-time use ssoToken is returned.</p>
     * <h2>Parameters</h2>
     * <p>
     * <ul>
     * <li>customerId - Required</li>
     * </ul>
     * </p>
     * <h2>Sample Request/Response</h2>
     * <p>
     * JSON Request
     * {â€œcustomerIdâ€: â€œXYZâ€}
     * JSON Response
     * {â€œErrorCodeâ€:0,â€ErrorMessageâ€:â€â€œ, â€œssoTokenâ€:â€ABC123â€}
     * </p>
     *
     * @param ingoAuthenticateOBORequest
     * @return
     */
    public IngoAuthenticateOBOResponse authenticateOBO(@Valid IngoAuthenticateOBORequest ingoAuthenticateOBORequest) {
        logger.info(ingoAuthenticateOBORequest.toString());

        AuthenticateOBO request = new AuthenticateOBO();
        BeanUtils.copyProperties(ingoAuthenticateOBORequest, request);

        AuthenticateOBOResponse authenticateOBOResponse = (AuthenticateOBOResponse) template.marshalSendAndReceive(request, getSoapActionCallBack(request, ingoAuthenticateOBORequest.getDeviceID(), ingoAuthenticateOBORequest.getSessionID()));

        IngoAuthenticateOBOResponse ingoAuthenticateOBOResponse = new IngoAuthenticateOBOResponse();
        if (!authenticateOBOResponse.getAuthenticateOBOResult().getSsoToken().isEmpty()) {
            ingoAuthenticateOBOResponse.setSsoToken(authenticateOBOResponse.getAuthenticateOBOResult().getSsoToken());
        } else {
            ingoAuthenticateOBOResponse.setErrorCode(authenticateOBOResponse.getAuthenticateOBOResult().getErrorCode().value());
            ingoAuthenticateOBOResponse.setErrorMessage(authenticateOBOResponse.getAuthenticateOBOResult().getErrorMessage());
        }

        return ingoAuthenticateOBOResponse;
    }

    /**
     * <h1>AddSessionAttributes</h1>
     */

    public IngoAddSessionAttributesResponse addSessionAttributes(@Valid IngoAddSessionAttributesRequest ingoAddSessionAttributesRequest) {
        logger.info(ingoAddSessionAttributesRequest.toString());

        AddSessionAttributes request = new AddSessionAttributes();

        ArrayOfSessionAttribute arrayOfSessionAttribute = new ArrayOfSessionAttribute();

        SessionAttribute attribute = new SessionAttribute();
        attribute.setName("SsoToken");
        attribute.setValue(ingoAddSessionAttributesRequest.getSsoToken());
        arrayOfSessionAttribute.getSessionAttribute().add(attribute);

        String preferredEmail = ingoAddSessionAttributesRequest.getPreferredEmail();
        if (preferredEmail != null && !preferredEmail.trim().isEmpty()) {
            attribute = new SessionAttribute();
            attribute.setName("PreferredEmail");
            attribute.setValue(preferredEmail);
            arrayOfSessionAttribute.getSessionAttribute().add(attribute);
        }

        StringBuilder sb = new StringBuilder();
        String[] accountIdFilter = ingoAddSessionAttributesRequest.getAccountIdFilter();
        if (accountIdFilter != null && accountIdFilter.length > 0) {
            sb.append(accountIdFilter[0].trim());
            for (int i = 1; i < accountIdFilter.length; i++) {
                sb.append("," + accountIdFilter[i].trim());
            }
            attribute = new SessionAttribute();
            attribute.setName("AccountIdFilter");
            attribute.setValue(sb.toString());
            arrayOfSessionAttribute.getSessionAttribute().add(attribute);
        }

        sb = new StringBuilder();
        String[] accountNumberFilter = ingoAddSessionAttributesRequest.getAccountNumberFilter();
        if (accountNumberFilter != null && accountNumberFilter.length > 0) {
            sb.append(accountNumberFilter[0].trim());
            for (int i = 1; i < accountNumberFilter.length; i++) {
                sb.append("," + accountNumberFilter[i].trim());
            }
            attribute = new SessionAttribute();
            attribute.setName("AccountNumberFilter");
            attribute.setValue(sb.toString());
            arrayOfSessionAttribute.getSessionAttribute().add(attribute);
        }
        request.setSessionAttributes(arrayOfSessionAttribute);


        AddSessionAttributesResponse addSessionAttributesResponse = (AddSessionAttributesResponse) template.marshalSendAndReceive(request, getSoapActionCallBack(request, ingoAddSessionAttributesRequest.getDeviceID(), ingoAddSessionAttributesRequest.getSessionID()));

        IngoAddSessionAttributesResponse ingoAddSessionAttributesResponse = new IngoAddSessionAttributesResponse();

        if (!addSessionAttributesResponse.getAddSessionAttributesResult().getErrorCode().value().equals(ErrorCode.NONE.value())) {
            ingoAddSessionAttributesResponse.setErrorCode(addSessionAttributesResponse.getAddSessionAttributesResult().getErrorCode().value());
            ingoAddSessionAttributesResponse.setErrorMessage(addSessionAttributesResponse.getAddSessionAttributesResult().getErrorMessage());
        }

        return ingoAddSessionAttributesResponse;
    }

    /**
     * @param deviceId
     * @param sessionId
     * @return WebServiceMessageCallback
     * @author Allen
     * Sets soap action on message and http headers
     */
    private WebServiceMessageCallback getSoapActionCallBack(final Object obj, final String deviceId, final String sessionId) {

        WebServiceMessageCallback soapActionCallback = new WebServiceMessageCallback() {
            public void doWithMessage(WebServiceMessage message) {
                TransportContext context = TransportContextHolder.getTransportContext();
                if (context.getConnection() instanceof HttpUrlConnection) {
                    HttpsURLConnection connection = (HttpsURLConnection) ((HttpUrlConnection) context.getConnection()).getConnection();

                    if (!StringUtils.isEmpty(sessionId)) {
                        connection.setRequestProperty("SessionId", sessionId);
                    }
                    connection.setRequestProperty("DeviceId", deviceId);

                }
                // these statements make it SoapActionCallBack
                SoapMessage soapMessage = (SoapMessage) message;
                soapMessage.setSoapAction(soapActionMap.get(obj.getClass()));
            }
        };

        return soapActionCallback;
    }


}