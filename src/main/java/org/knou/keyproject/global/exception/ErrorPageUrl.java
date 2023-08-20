package org.knou.keyproject.global.exception;

// 2023.8.20(일) 15h55
public interface ErrorPageUrl {
    // Error View Name & Page URLs
    String ERROR_VIEW_NAME = "common/errorPage"; // Modifier 'static' is redundant for interface fields

    String ERROR_400_URL = "/common/errorPage/400"; // bad request
    String ERROR_401_URL = "/common/errorPage/401"; // unauthorized
    String ERROR_403_URL = "/common/errorPage/403"; // forbidden
    String ERROR_404_URL = "/common/errorPage/404"; // not found
    String ERROR_405_URL = "/common/errorPage/405"; // method not allowed
    String ERROR_409_URL = "/common/errorPage/409"; // conflict
    String ERROR_500_URL = "/common/errorPage/500"; // internal server error
}
