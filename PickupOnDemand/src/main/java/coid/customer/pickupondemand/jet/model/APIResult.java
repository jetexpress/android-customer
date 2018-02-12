package coid.customer.pickupondemand.jet.model;

public class APIResult
{
    private String statusCode;
    private String message;
    private String data;
    private ErrorResult errors;

    public String getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public ErrorResult getErrors()
    {
        return errors;
    }

    public void setErrors(ErrorResult errors)
    {
        this.errors = errors;
    }

    public boolean isError()
    {
        return errors != null;
    }
}
