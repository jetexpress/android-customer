package coid.customer.pickupondemand.jet.model.db;

public class DBContract
{
    public static class LoginEntry
    {
        public static final String TABLE_NAME = "Login";
        public static final String COLUMN_ACCESS_TOKEN = "AccessToken";
        public static final String COLUMN_TOKEN_TYPE = "TokenType";
        public static final String COLUMN_EXPIRES_IN = "ExpiresIn";
        public static final String COLUMN_REFRESH_TOKEN = "RefreshToken";
    }

    public static class UserProfileEntry
    {
        public static final String TABLE_NAME = "UserProfile";
        public static final String COLUMN_USERNAME = "Username";
        public static final String COLUMN_FULL_NAME = "FullName";
        public static final String COLUMN_DATE_OF_BIRTH = "DateOfBirth";
        public static final String COLUMN_DATE_OF_BIRTH_IN_MILLIS = "DateOfBirthInMillis";
        public static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
        public static final String COLUMN_ADDRESS = "Address";
        public static final String COLUMN_EMAIL = "Email";
    }

    public static class WalletEntry
    {
        public static final String TABLE_NAME = "Wallet";
        public static final String COLUMN_BONUS_CREDIT = "BonusCredit";
        public static final String COLUMN_TOP_UP_CREDIT = "TopUpCredit";
        public static final String FK_USER_PROFILE_ID = "UserProfileId";
    }

    public static class NotificationPayloadEntry
    {
        public static final String TABLE_NAME = "NotificationPayload";
        public static final String COLUMN_CODE = "Code";
        public static final String COLUMN_ROLE = "Role";
        public static final String COLUMN_NOTIFICATION_TIME = "NotificationTime";
        public static final String COLUMN_RATE = "Rate";
        public static final String COLUMN_TYPE = "Type";
        public static final String COLUMN_STATUS = "Status";
        public static final String COLUMN_IS_UNREAD = "IsUnread";
        public static final String COLUMN_IS_NEW = "IsNew";
    }
}
