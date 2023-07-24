public with sharing class DemoController {

    @AuraEnabled
    public static List<Demo> getDemos() {
        List<Demo__c> records = [SELECT Name FROM Demo__c WITH USER_MODE ORDER BY Name ASC];
        List<Demo> demos = new List<Demo>();
        for (Demo__c record : records) {
            demos.add(new Demo(record.Name, null));
        }
        return demos;
    }

    public class Demo {

        @AuraEnabled
        public String name { get; private set; }
    
        @AuraEnabled
        public String description { get; private set; }
    
        public Demo(String name, String description) {
            this.name = name;
            this.description = description;
        }
    
    }
}