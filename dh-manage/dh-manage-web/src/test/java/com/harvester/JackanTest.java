package com.harvester;

import com.google.common.collect.Lists;
import eu.trentorise.opendata.jackan.CheckedCkanClient;
import eu.trentorise.opendata.jackan.CkanClient;
import eu.trentorise.opendata.jackan.CkanQuery;
import eu.trentorise.opendata.jackan.dcat.DcatFactory;
import eu.trentorise.opendata.jackan.model.*;
import eu.trentorise.opendata.traceprov.dcat.DcatDataset;
import eu.trentorise.opendata.traceprov.dcat.DcatDistribution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by cui on 2017/11/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext*.xml"})
public class JackanTest {

    @Test
    public void test() {
        CkanClient cc = new CheckedCkanClient("http://159.226.158.220:8088/", "ea8324ce-b07a-4616-b6f8-35f7ddbd57ad");

        System.out.println("*******************" + cc.getDatasetList());
        List<String> ds = cc.getDatasetList();
        for (String s : ds) {
            System.out.println();
            System.out.println("DATASET: " + s);
            CkanDataset d = cc.getDataset(s);
            System.out.println("  RESOURCES:");
            for (CkanResource r : d.getResources()) {
                System.out.println("    " + r.getName());
                System.out.println("    FORMAT: " + r.getFormat());
                System.out.println("       URL: " + r.getUrl());
            }
        }

    }

    @Test
    public void testFilter() {
        CkanClient cc = new CkanClient("http://159.226.158.220:8088/");
//        .byTagNames("ristoranti")
        CkanQuery query = CkanQuery.filter().byGroupNames("Dave's books");
        List<CkanDataset> filteredDatasets = cc.searchDatasets(query, 10, 0).getResults();

        for (CkanDataset d : filteredDatasets) {
            System.out.println();
            System.out.println("DATASET: " + d.getName());
        }
    }

    @Test
    public void testCreateDataset() {
        CkanClient myClient = new CheckedCkanClient("http://159.226.158.220:8088/", "ea8324ce-b07a-4616-b6f8-35f7ddbd57ad");

//        CkanOrganization organization = myClient.createOrganization(new CkanOrganization("msdc"));
//        String newOrgId = organization.getId();
//        System.out.println("create new organization:" + organization.getId());
//
        CkanDatasetBase dataset = new CkanDatasetBase();
        dataset.setOwnerOrg("b66eba5e-cb2e-41f4-b4e4-74e318faeb48");
        dataset.setTitle("中文测试名称");
        dataset.setName("testt");
        dataset.setNotes("测试介绍文字！");
        dataset.setAuthor("作者");

        CkanResource resource=new CkanResource();
        resource.setUrl("http://my-department.org/expenses.csv");
        resource.setName("测试一下子");
        resource.setResourceType("csv");
        resource.setDescription("描述信息描述信息");
        List<CkanResource> lstResource= Lists.newArrayList();
        lstResource.add(resource);

        dataset.setResources(lstResource);

        CkanDataset createdDataset = myClient.createDataset(dataset);
        System.out.println("create new dataset:"+createdDataset.getId());
    }

    @Test
    public void testDelDataset(){
        CkanClient myClient = new CheckedCkanClient("http://159.226.158.220:8088/", "ea8324ce-b07a-4616-b6f8-35f7ddbd57ad");
        myClient.deleteDataset("bc9f6c1c-4461-4332-adab-0cb6de613dd3");
    }
}
