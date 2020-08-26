import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FilterRuleService } from 'app/entities/filter-rule/filter-rule.service';
import { IFilterRule, FilterRule } from 'app/shared/model/filter-rule.model';
import { RuleField } from 'app/shared/model/enumerations/rule-field.model';
import { RuleOperator } from 'app/shared/model/enumerations/rule-operator.model';

describe('Service Tests', () => {
  describe('FilterRule Service', () => {
    let injector: TestBed;
    let service: FilterRuleService;
    let httpMock: HttpTestingController;
    let elemDefault: IFilterRule;
    let expectedResult: IFilterRule | IFilterRule[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FilterRuleService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new FilterRule(0, RuleField.AMOUNT, RuleOperator.EQUALS, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FilterRule', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FilterRule()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FilterRule', () => {
        const returnedFromService = Object.assign(
          {
            field: 'BBBBBB',
            operator: 'BBBBBB',
            stringValue: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FilterRule', () => {
        const returnedFromService = Object.assign(
          {
            field: 'BBBBBB',
            operator: 'BBBBBB',
            stringValue: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FilterRule', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
