@IsTest
public class TestUtility {
  public static Profile getStandardProfile() {
    return [
      SELECT Id, Name
      FROM Profile
      WHERE Name IN ('Standard User', '標準ユーザ', '標準ユーザー')
    ];
  }

  public static User demoUser(Profile p) {
    String subPartOfUsername = DateTime.now().format('HHmmss_SSS');
    // NOTE: alias: max size is 8
    String alias = DateTime.now().format('mmss_SSS');
    String baseUsername = UserInfo.getUserName();
    List<String> partsOfUsername = baseUsername.split('@');
    partsOfUsername.add(subPartOfUsername);
    String demoUsername = String.format('{0}-{2}@{1}', partsOfUsername);

    return new User(
      UserName = demoUsername,
      Email = demoUsername,
      LastName = subPartOfUsername,
      Alias = alias,
      TimeZoneSidKey = 'asia/tokyo',
      LocaleSidKey = 'ja_JP',
      EmailEncodingKey = 'UTF-8',
      LanguageLocaleKey = 'ja',
      ProfileId = p.Id
    );
  }

  public static User createDemoUser(Profile p) {
    User u = demoUser(p);
    insert u;

    return u;
  }

  public static PermissionSet getPermissionSet(String name) {
    return [
      SELECT Id
      FROM PermissionSet
      WHERE NamespacePrefix = 'demo_aho' AND Name = :name
    ];
  }

  public static void assignPermission(User u, String permissionSetName) {
    PermissionSet ps = getPermissionSet(permissionSetName);
    insert new PermissionSetAssignment(
      AssigneeId = u.Id,
      PermissionSetId = ps.Id
    );
  }
}
