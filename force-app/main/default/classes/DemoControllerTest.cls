@IsTest
private class DemoControllerTest {
    @IsTest
    static void getDemos() {

        Profile p = [SELECT Id, Name FROM Profile WHERE Name IN ('Standard User', '標準ユーザ')];

        User u = demoUser(p);
        insert u;

        PermissionSet ps = [SELECT Id FROM PermissionSet WHERE Name = 'demo'];
        insert new PermissionSetAssignment(AssigneeId = u.Id, PermissionSetId = ps.Id);

        System.runAs(u) {
            List<Demo__c> records = new List<Demo__c>{
                new Demo__c(Name = 'Foo'),
                new Demo__c(Name = 'Bar', description__c = 'Bar description')
            };
            insert records;

            Test.startTest();

            List<DemoController.Demo> actual = DemoController.getDemos();
    
            System.assertEquals(2, actual.size());
    
            DemoController.Demo actualThat = actual.get(0);
            System.assertEquals(true, actualThat.id != null);
            System.assertEquals('Bar', actualThat.name);
            System.assertEquals(null, actualThat.description);
    
            actualThat = actual.get(1);
            System.assertEquals(true, actualThat.id != null);
            System.assertEquals('Foo', actualThat.name);
            System.assertEquals(null, actualThat.description);
    
            Test.stopTest();
        }

    }

    private static User demoUser(Profile p) {
        String baseUsername = UserInfo.getUserName();
        List<String> partsOfUsername = baseUsername.split('@');
        String demoUsername = String.format('{0}-demo@{1}', partsOfUsername);

        return new User(UserName = demoUsername,
            Email = demoUsername,
            LastName = 'demo',
            Alias = 'demo',
            TimeZoneSidKey = 'asia/tokyo',
            LocaleSidKey = 'ja_JP',
            EmailEncodingKey = 'UTF-8',
            LanguageLocaleKey = 'ja',
            ProfileId = p.Id
        );

    }
}