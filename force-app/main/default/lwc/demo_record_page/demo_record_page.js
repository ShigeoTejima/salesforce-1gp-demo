import { LightningElement, api, wire } from "lwc";
import { gql, graphql } from "lightning/uiGraphQLApi";

export default class Demo_record_page extends LightningElement {
  @api recordId;

  @wire(graphql, {
    query: gql`
      query queryName($recordId: ID) {
        uiapi {
          query {
            demo_aho__Demo__c(where: { Id: { eq: $recordId } }) {
              edges {
                node {
                  Id
                  Name {
                    value
                  }
                  demo_aho__description__c {
                    value
                  }
                }
              }
            }
          }
        }
      }
    `,
    variables: "$variables"
  })
  graphqlQueryResult({ data, errors }) {
    if (errors) {
      console.error(errors);
    } else if (data) {
      console.log(data);
      this.records = data.uiapi.query.demo_aho__Demo__c.edges.map(
        (edge) => edge.node
      );
    }
    this.errors = errors;
  }

  get variables() {
    return {
      recordId: this.recordId
    };
  }

  records;
  errors;

  get firstName() {
    if (this.records == null) {
      return null;
    }
    if (this.records.length === 0) {
      return null;
    }

    return this.records[0].Name.value;
  }
}
