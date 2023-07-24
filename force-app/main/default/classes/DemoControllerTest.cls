@IsTest
private class DemoControllerTest {
    @IsTest
    static void getDemos() {

        User u = new User(ID = UserInfo.getUserID());

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
            System.assertEquals('Bar', actualThat.name);
            System.assertEquals(null, actualThat.description);
    
            actualThat = actual.get(1);
            System.assertEquals('Foo', actualThat.name);
            System.assertEquals(null, actualThat.description);
    
            Test.stopTest();
        }

    }
}