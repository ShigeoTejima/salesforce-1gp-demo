public with sharing class DemoController {

    @AuraEnabled(cacheable = true)
    public static List<Demo> getDemos() {
        List<Demo__c> records = [SELECT Id, Name, description__c FROM Demo__c WITH USER_MODE ORDER BY Name ASC];
        List<Demo> demos = new List<Demo>();
        for (Demo__c record : records) {
            demos.add(new Demo(record.Id, record.Name, record.description__c));
        }
        return demos;
    }

    public class Demo {

        @AuraEnabled
        public String id { get; private set; }

        @AuraEnabled
        public String name { get; private set; }
    
        @AuraEnabled
        public String description { get; private set; }
    
        public Demo(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    
    }
}