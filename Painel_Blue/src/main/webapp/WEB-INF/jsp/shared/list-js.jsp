<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tmpl" uri="/WEB-INF/tld/template.tld" %>

<c:set var="request" value="<%=request%>" />
<c:set var="response" value="<%=response%>" />

<script>
    function List(options) {
        list = this;

        list.settings = $.extend({
            'url': '/',
            'method': 'GET',
            'success': function () {
            },
            'error': function () {
            }
        }, options);

        list.filterField = '';
        list.filterValue = '';
        list.filterType = '';
        list.orderField = 'id';
        list.order = 'ASC';
        list.page = 0;

        list.refreshList = function () {
            $.ajax({
                url: list.settings.url,
                type: list.settings.method,
                data: {
                    'filterField': list.filterField,
                    'filterValue': list.filterValue,
                    'filterType': list.filterType,
                    'orderField': list.orderField,
                    'order': list.order,
                    'page': list.page
                },
                success: function (data) {
                    list.settings.success(data);
                },
                error: list.settings.error
            });
        };

        list.filter = function (filterField, filterValue, filterType) {
            list.filterField = filterField;
            list.filterValue = filterValue;
            list.filterType = filterType;

            list.orderField = 'id';
            list.order = 'ASC';
            list.page = 0;

            list.refreshList();
        };

        list.reset = function () {
            list.filterField = '';
            list.filterValue = '';
            list.orderField = 'id';
            list.order = 'ASC';
            list.page = 0;

            list.refreshList();
        };

        list.setOrder = function (orderField) {
            if (list.orderField == orderField) {
                if (list.order == 'ASC') {
                    list.order = 'DESC';
                }
                else {
                    list.order = 'ASC';
                }
            }
            else {
                list.order = 'ASC';
            }

            list.orderField = orderField;
            list.page = 0;

            list.refreshList();
        };

        list.prevPage = function () {
            if (list.page > 0) {
                list.page--;

                list.refreshList();
            }
        };

        list.nextPage = function () {
            if (list.page < list.numOfPages) {
                list.page++;

                list.refreshList();
            }
        };

        list.setPage = function (page) {
            list.page = page;

            list.refreshList();
        };
    }
</script>