package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.bean.Order;
import com.example.bean.Path;
import com.example.bean.WorkflowFile;
import com.example.core.ResourceAllocation;
import com.example.core.ServiceComposition;
import com.example.service.CustomerService;
import com.example.service.OrderService;
import com.example.service.PathService;
import com.example.service.WorkflowFileService;
import net.sf.json.JSONArray;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/26 11:24
 * @Description:
 * @FileName: AjaxController
 */
@Controller
@RequestMapping("/ajax")
public class AjaxController {

    @Resource
    CustomerService customerService;

    @Resource
    OrderService orderService;

    @Resource
    PathService pathService;

    @Resource
    WorkflowFileService workflowFileService;

    @Resource
    ServiceComposition serviceComposition;

    @Resource
    ResourceAllocation resourceAllocation;

    /**
     *  ServiceComposition
     **/
    @ResponseBody
    @PostMapping("/generate")
    public List<List<Double>> generatePath(Order order, String objective) {
        order.setWeight(0);
        List<List<Double>> shortestPaths = serviceComposition.selectStrategyByObjective(order,
                objective, 2, 2);
        if (shortestPaths == null) {
            return null;
        }
        List<Double> desLocation = customerService.getLocationByName(order.getConsignee());
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(shortestPaths);
        jsonArray.add(desLocation);
        return jsonArray;
    }

    /**
     *  ResourceAllocation
     **/
    @ResponseBody
    @PostMapping("/getRoute")
    public List<List<Double>> getRoute(int uavType, int ugvType, Order order, String objective) {
        JSONArray jsonArray = new JSONArray();
        List<List<Double>> shortestPaths = resourceAllocation.selectStrategyByObjective(order, objective, uavType, ugvType);
        if (shortestPaths == null) {
            return null;
        } else if (shortestPaths.size() == 1) {
            return shortestPaths;
        }
        List<Double> desLocation = customerService.getLocationByName(order.getConsignee());
        jsonArray.addAll(shortestPaths);
        jsonArray.add(desLocation);
        return jsonArray;
    }

    /**
     *  Whether to select Workflow
     **/
    @ResponseBody
    @GetMapping("/getFileStatus")
    public String getFileStatus() {
        WorkflowFile byId = workflowFileService.getById(1);
        return String.valueOf(byId.getStatus());
    }

    /**
     *  upload workflow file
     **/
    @ResponseBody
    @PostMapping(path = "/uploadFile")
    public String uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        if (file != null) {
            WorkflowFile byId = workflowFileService.getById(1);
            byId.setStatus(1);
            workflowFileService.update(byId, null);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String line;
                StringBuffer content = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            } catch (IOException e) {

            } finally {
                if (reader != null) {
                    IOUtils.closeQuietly(reader);
                }
            }
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * delete order
     **/
    @ResponseBody
    @DeleteMapping("/deleteOrder/{id}")
    public int deleteOrder(@PathVariable int id) {
        Order order = orderService.getById(id);
        QueryWrapper<Path> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", order.getOrderId());
        boolean remove = pathService.remove(queryWrapper);
        boolean b = orderService.removeById(id);
        if (remove == b == true) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * order workflow file example
     **/
    @ResponseBody
    @GetMapping("/workflow")
    public String getWorkflow1() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn:definitions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" id=\"Definitions_0iwwsjh\" targetNamespace=\"http://bpmn.io/schema/bpmn\" exporter=\"bpmn-js (https://demo.bpmn.io)\" exporterVersion=\"10.2.0\">\n" +
                "  <bpmn:process id=\"Process_0vzn05x\" isExecutable=\"false\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_15kfb66\">\n" +
                "      <bpmn:outgoing>Flow_0bbu9qg</bpmn:outgoing>\n" +
                "    </bpmn:startEvent>\n" +
                "    <bpmn:task id=\"Activity_10wknde\" name=\"Task Assignment\">\n" +
                "      <bpmn:incoming>Flow_0bbu9qg</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1iz6pco</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>Flow_13aq842</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:task id=\"Activity_0fqeclr\" name=\"Environment Mapping\">\n" +
                "      <bpmn:incoming>Flow_1iz6pco</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_0jgiewh</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:task id=\"Activity_05lyl17\" name=\"Location Estmate\">\n" +
                "      <bpmn:incoming>Flow_0jgiewh</bpmn:incoming>\n" +
                "      <bpmn:incoming>Flow_1mbxsxw</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_127iyfc</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0bbu9qg\" sourceRef=\"StartEvent_15kfb66\" targetRef=\"Activity_10wknde\" />\n" +
                "    <bpmn:task id=\"Activity_0ux2h8g\" name=\"Map Retrieval\">\n" +
                "      <bpmn:incoming>Flow_13aq842</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1mbxsxw</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:task id=\"Activity_1x66ckk\" name=\"Path Plan Picker\">\n" +
                "      <bpmn:incoming>Flow_127iyfc</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1pwaqwx</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>Flow_0k3jjof</bpmn:outgoing>\n" +
                "      <bpmn:outgoing>Flow_0p615ng</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:task id=\"Activity_0zwtys2\" name=\"Movement\">\n" +
                "      <bpmn:incoming>Flow_0k3jjof</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1lnhc28</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:task id=\"Activity_149vm6b\" name=\"Map Generation\">\n" +
                "      <bpmn:incoming>Flow_1pwaqwx</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_046on3x</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:task id=\"Activity_1jajch6\" name=\"Obstacle Avoidance\">\n" +
                "      <bpmn:incoming>Flow_0p615ng</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_0rs4sz5</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:task id=\"Activity_0l5sf66\" name=\"Picker Destination\">\n" +
                "      <bpmn:incoming>Flow_1lnhc28</bpmn:incoming>\n" +
                "      <bpmn:incoming>Flow_046on3x</bpmn:incoming>\n" +
                "      <bpmn:incoming>Flow_0rs4sz5</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1tn235x</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:task id=\"Activity_1aoxbe5\" name=\"Place Object\">\n" +
                "      <bpmn:incoming>Flow_1tn235x</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_0s1v9zt</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:endEvent id=\"Event_1wglv1p\">\n" +
                "      <bpmn:incoming>Flow_0s1v9zt</bpmn:incoming>\n" +
                "    </bpmn:endEvent>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1iz6pco\" sourceRef=\"Activity_10wknde\" targetRef=\"Activity_0fqeclr\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_13aq842\" sourceRef=\"Activity_10wknde\" targetRef=\"Activity_0ux2h8g\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0jgiewh\" sourceRef=\"Activity_0fqeclr\" targetRef=\"Activity_05lyl17\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1mbxsxw\" sourceRef=\"Activity_0ux2h8g\" targetRef=\"Activity_05lyl17\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_127iyfc\" sourceRef=\"Activity_05lyl17\" targetRef=\"Activity_1x66ckk\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1pwaqwx\" sourceRef=\"Activity_1x66ckk\" targetRef=\"Activity_149vm6b\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0k3jjof\" sourceRef=\"Activity_1x66ckk\" targetRef=\"Activity_0zwtys2\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0p615ng\" sourceRef=\"Activity_1x66ckk\" targetRef=\"Activity_1jajch6\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1lnhc28\" sourceRef=\"Activity_0zwtys2\" targetRef=\"Activity_0l5sf66\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_046on3x\" sourceRef=\"Activity_149vm6b\" targetRef=\"Activity_0l5sf66\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0rs4sz5\" sourceRef=\"Activity_1jajch6\" targetRef=\"Activity_0l5sf66\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1tn235x\" sourceRef=\"Activity_0l5sf66\" targetRef=\"Activity_1aoxbe5\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0s1v9zt\" sourceRef=\"Activity_1aoxbe5\" targetRef=\"Event_1wglv1p\" />\n" +
                "  </bpmn:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"Process_0vzn05x\">\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_0yote01\" bpmnElement=\"Activity_05lyl17\">\n" +
                "        <dc:Bounds x=\"510\" y=\"170\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_1pngvo6\" bpmnElement=\"Activity_1x66ckk\">\n" +
                "        <dc:Bounds x=\"630\" y=\"170\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_1dr8xmd\" bpmnElement=\"Activity_149vm6b\">\n" +
                "        <dc:Bounds x=\"750\" y=\"170\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_0vitb1c\" bpmnElement=\"Activity_0zwtys2\">\n" +
                "        <dc:Bounds x=\"750\" y=\"60\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_16fux4i\" bpmnElement=\"Activity_0l5sf66\">\n" +
                "        <dc:Bounds x=\"870\" y=\"170\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_0cy0emu\" bpmnElement=\"Activity_1aoxbe5\">\n" +
                "        <dc:Bounds x=\"990\" y=\"170\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_1wglv1p_di\" bpmnElement=\"Event_1wglv1p\">\n" +
                "        <dc:Bounds x=\"1112\" y=\"192\" width=\"36\" height=\"36\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_1geucmf\" bpmnElement=\"Activity_1jajch6\">\n" +
                "        <dc:Bounds x=\"750\" y=\"280\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_1tninp9\" bpmnElement=\"Activity_0ux2h8g\">\n" +
                "        <dc:Bounds x=\"380\" y=\"280\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_1ucjep5\" bpmnElement=\"Activity_0fqeclr\">\n" +
                "        <dc:Bounds x=\"380\" y=\"60\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_10wknde_di\" bpmnElement=\"Activity_10wknde\">\n" +
                "        <dc:Bounds x=\"250\" y=\"170\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_15kfb66\">\n" +
                "        <dc:Bounds x=\"192\" y=\"192\" width=\"36\" height=\"36\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0bbu9qg_di\" bpmnElement=\"Flow_0bbu9qg\">\n" +
                "        <di:waypoint x=\"228\" y=\"210\" />\n" +
                "        <di:waypoint x=\"250\" y=\"210\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1iz6pco_di\" bpmnElement=\"Flow_1iz6pco\">\n" +
                "        <di:waypoint x=\"350\" y=\"210\" />\n" +
                "        <di:waypoint x=\"365\" y=\"210\" />\n" +
                "        <di:waypoint x=\"365\" y=\"100\" />\n" +
                "        <di:waypoint x=\"380\" y=\"100\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_13aq842_di\" bpmnElement=\"Flow_13aq842\">\n" +
                "        <di:waypoint x=\"350\" y=\"210\" />\n" +
                "        <di:waypoint x=\"365\" y=\"210\" />\n" +
                "        <di:waypoint x=\"365\" y=\"320\" />\n" +
                "        <di:waypoint x=\"380\" y=\"320\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0jgiewh_di\" bpmnElement=\"Flow_0jgiewh\">\n" +
                "        <di:waypoint x=\"480\" y=\"100\" />\n" +
                "        <di:waypoint x=\"495\" y=\"100\" />\n" +
                "        <di:waypoint x=\"495\" y=\"210\" />\n" +
                "        <di:waypoint x=\"510\" y=\"210\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1mbxsxw_di\" bpmnElement=\"Flow_1mbxsxw\">\n" +
                "        <di:waypoint x=\"480\" y=\"320\" />\n" +
                "        <di:waypoint x=\"495\" y=\"320\" />\n" +
                "        <di:waypoint x=\"495\" y=\"210\" />\n" +
                "        <di:waypoint x=\"510\" y=\"210\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_127iyfc_di\" bpmnElement=\"Flow_127iyfc\">\n" +
                "        <di:waypoint x=\"610\" y=\"210\" />\n" +
                "        <di:waypoint x=\"630\" y=\"210\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1pwaqwx_di\" bpmnElement=\"Flow_1pwaqwx\">\n" +
                "        <di:waypoint x=\"730\" y=\"210\" />\n" +
                "        <di:waypoint x=\"750\" y=\"210\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0k3jjof_di\" bpmnElement=\"Flow_0k3jjof\">\n" +
                "        <di:waypoint x=\"680\" y=\"170\" />\n" +
                "        <di:waypoint x=\"680\" y=\"100\" />\n" +
                "        <di:waypoint x=\"750\" y=\"100\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0p615ng_di\" bpmnElement=\"Flow_0p615ng\">\n" +
                "        <di:waypoint x=\"680\" y=\"250\" />\n" +
                "        <di:waypoint x=\"680\" y=\"320\" />\n" +
                "        <di:waypoint x=\"750\" y=\"320\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1lnhc28_di\" bpmnElement=\"Flow_1lnhc28\">\n" +
                "        <di:waypoint x=\"850\" y=\"100\" />\n" +
                "        <di:waypoint x=\"920\" y=\"100\" />\n" +
                "        <di:waypoint x=\"920\" y=\"170\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_046on3x_di\" bpmnElement=\"Flow_046on3x\">\n" +
                "        <di:waypoint x=\"850\" y=\"210\" />\n" +
                "        <di:waypoint x=\"870\" y=\"210\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0rs4sz5_di\" bpmnElement=\"Flow_0rs4sz5\">\n" +
                "        <di:waypoint x=\"850\" y=\"320\" />\n" +
                "        <di:waypoint x=\"920\" y=\"320\" />\n" +
                "        <di:waypoint x=\"920\" y=\"250\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1tn235x_di\" bpmnElement=\"Flow_1tn235x\">\n" +
                "        <di:waypoint x=\"970\" y=\"210\" />\n" +
                "        <di:waypoint x=\"990\" y=\"210\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0s1v9zt_di\" bpmnElement=\"Flow_0s1v9zt\">\n" +
                "        <di:waypoint x=\"1090\" y=\"210\" />\n" +
                "        <di:waypoint x=\"1112\" y=\"210\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn:definitions>\n";
    }

    /**
     * system workflow file example
     **/
    @ResponseBody
    @GetMapping("/workflow1")
    public String getWorkflow() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<bpmn:definitions xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" id=\"Definitions_0iwwsjh\" targetNamespace=\"http://bpmn.io/schema/bpmn\" exporter=\"bpmn-js (https://demo.bpmn.io)\" exporterVersion=\"10.2.0\">\n" +
                "  <bpmn:process id=\"Process_0vzn05x\" isExecutable=\"false\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_15kfb66\">\n" +
                "      <bpmn:outgoing>Flow_0bbu9qg</bpmn:outgoing>\n" +
                "    </bpmn:startEvent>\n" +
                "    <bpmn:task id=\"Activity_10wknde\" name=\"Order Information (Analyzer)\">\n" +
                "      <bpmn:incoming>Flow_0bbu9qg</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_1v01pm7</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:task id=\"Activity_0fqeclr\" name=\"Service Composition\">\n" +
                "      <bpmn:incoming>Flow_1v01pm7</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_18rdfp1</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:task id=\"Activity_05lyl17\" name=\"Service Allocation\">\n" +
                "      <bpmn:incoming>Flow_18rdfp1</bpmn:incoming>\n" +
                "      <bpmn:outgoing>Flow_0iah69p</bpmn:outgoing>\n" +
                "    </bpmn:task>\n" +
                "    <bpmn:endEvent id=\"Event_156iws9\">\n" +
                "      <bpmn:incoming>Flow_0iah69p</bpmn:incoming>\n" +
                "    </bpmn:endEvent>\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0bbu9qg\" sourceRef=\"StartEvent_15kfb66\" targetRef=\"Activity_10wknde\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_1v01pm7\" name=\"Service Requirements\" sourceRef=\"Activity_10wknde\" targetRef=\"Activity_0fqeclr\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_18rdfp1\" name=\"Compostion Plan\" sourceRef=\"Activity_0fqeclr\" targetRef=\"Activity_05lyl17\" />\n" +
                "    <bpmn:sequenceFlow id=\"Flow_0iah69p\" name=\"Allocation Plan\" sourceRef=\"Activity_05lyl17\" targetRef=\"Event_156iws9\" />\n" +
                "  </bpmn:process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_1\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_1\" bpmnElement=\"Process_0vzn05x\">\n" +
                "      <bpmndi:BPMNShape id=\"_BPMNShape_StartEvent_2\" bpmnElement=\"StartEvent_15kfb66\">\n" +
                "        <dc:Bounds x=\"156\" y=\"81\" width=\"36\" height=\"36\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_10wknde_di\" bpmnElement=\"Activity_10wknde\">\n" +
                "        <dc:Bounds x=\"270\" y=\"59\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_1ucjep5\" bpmnElement=\"Activity_0fqeclr\">\n" +
                "        <dc:Bounds x=\"479\" y=\"59\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_0yote01\" bpmnElement=\"Activity_05lyl17\">\n" +
                "        <dc:Bounds x=\"680\" y=\"59\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_156iws9_di\" bpmnElement=\"Event_156iws9\">\n" +
                "        <dc:Bounds x=\"870\" y=\"81\" width=\"36\" height=\"36\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0bbu9qg_di\" bpmnElement=\"Flow_0bbu9qg\">\n" +
                "        <di:waypoint x=\"192\" y=\"99\" />\n" +
                "        <di:waypoint x=\"270\" y=\"99\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1v01pm7_di\" bpmnElement=\"Flow_1v01pm7\">\n" +
                "        <di:waypoint x=\"370\" y=\"99\" />\n" +
                "        <di:waypoint x=\"479\" y=\"99\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"385\" y=\"66\" width=\"69\" height=\"27\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_18rdfp1_di\" bpmnElement=\"Flow_18rdfp1\">\n" +
                "        <di:waypoint x=\"579\" y=\"99\" />\n" +
                "        <di:waypoint x=\"680\" y=\"99\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"584\" y=\"81\" width=\"85\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0iah69p_di\" bpmnElement=\"Flow_0iah69p\">\n" +
                "        <di:waypoint x=\"780\" y=\"99\" />\n" +
                "        <di:waypoint x=\"870\" y=\"99\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <dc:Bounds x=\"784\" y=\"81\" width=\"73\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</bpmn:definitions>\n";
    }

}