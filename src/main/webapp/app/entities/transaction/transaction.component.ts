import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransaction } from 'app/shared/model/transaction.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TransactionService } from './transaction.service';
import { TransactionDeleteDialogComponent } from './transaction-delete-dialog.component';
import { IBankAccount } from '../../shared/model/bank-account.model';
import { BankAccountService } from '../bank-account/bank-account.service';
import { ISubCategory } from '../../shared/model/sub-category.model';
import { ICategory } from '../../shared/model/category.model';
import { SubCategoryService } from '../sub-category/sub-category.service';
import { CategoryService } from '../category/category.service';
import { Moment } from 'moment';
import { DATE_FORMAT } from '../../shared/constants/input.constants';

type SelectableEntity = IBankAccount | ISubCategory | ICategory;

@Component({
  selector: 'jhi-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss'],
})
export class TransactionComponent implements OnInit, OnDestroy {
  transactions?: ITransaction[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  dateFormat = DATE_FORMAT;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  labelSearch = '';
  minDateSearch?: Moment;
  maxDateSearch?: Moment;
  minAmountSearch?: number;
  maxAmountSearch?: number;
  bankAccounts: IBankAccount[] = [];
  bankAccountSearch?: IBankAccount;
  categories: ICategory[] = [];
  categorySearch?: ICategory;
  subcategories: ISubCategory[] = [];
  subCategorySearch?: ISubCategory;
  fullCriteria = {};

  constructor(
    protected transactionService: TransactionService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected bankAccountService: BankAccountService,
    protected categoryService: CategoryService,
    protected subCategoryService: SubCategoryService
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.transactionService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        ...this.fullCriteria,
      })
      .subscribe(
        (res: HttpResponse<ITransaction[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  search(jpaFilter: string, query: any): void {
    if (!jpaFilter) {
      this.fullCriteria = {};
    } else {
      if (query) {
        this.fullCriteria[jpaFilter] = query;
      } else {
        delete this.fullCriteria[jpaFilter];
      }
    }
    this.loadPage();
  }

  ngOnInit(): void {
    this.bankAccountService.query().subscribe((res: HttpResponse<IBankAccount[]>) => (this.bankAccounts = res.body || []));
    this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
    this.subCategoryService.query().subscribe((res: HttpResponse<ISubCategory[]>) => (this.subcategories = res.body || []));
    this.handleNavigation();
    this.registerChangeInTransactions();
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITransaction): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  registerChangeInTransactions(): void {
    this.eventSubscriber = this.eventManager.subscribe('transactionListModification', () => this.loadPage());
  }

  delete(transaction: ITransaction): void {
    const modalRef = this.modalService.open(TransactionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.transaction = transaction;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: ITransaction[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/transaction'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.transactions = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
