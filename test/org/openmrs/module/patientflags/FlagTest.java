/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.patientflags;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.Cohort;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientflags.api.FlagService;
import org.openmrs.test.BaseContextSensitiveTest;

public class FlagTest extends BaseContextSensitiveTest{
	
	/**
	 * Tests of the Flag class
	 */
	
	/**
	 * setEvaluator method tests
	 */
	@Before
	public void initTestData() throws Exception{		
		initializeInMemoryDatabase();
		executeDataSet("org/openmrs/module/patientflags/include/FlagTest.xml");
		authenticate();
	}
	
	@Test
	public void setEvaluator_shouldSetLogicFlagEvaluator() throws Exception{	
		Flag flag = Context.getService(FlagService.class).getFlag(1);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("logic"));
		Assert.assertEquals(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("logic"), flag.getEvaluator());
	}
	
	@Test
	public void setEvaluator_shouldSetSQLFlagEvaluator() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(1);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("sql"));
		Assert.assertEquals(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("sql"), flag.getEvaluator());
	}
	
	@Test
	public void setEvaluator_shouldSetGroovyFlagEvaluator() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(1);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("groovy"));
		Assert.assertEquals(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("groovy"), flag.getEvaluator());
	}
	
	
	@Test
	public void setEvaluator_shouldFailOnInvalidEvaluatorType() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(1);
		
		// this should throw an APIException
		try{
			flag.setEvaluator("bogus");
			// if the code gets here, it didn't throw exception
			Assert.fail();
		}
		catch (Exception e){
			
		}
	}
	
	/**
	 * validate() method tests
	 */
	
	@Test
	public void validate_shouldReturnNullIfNoEvaluator() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(1);
		Assert.assertNull(flag.validate());
	}
	
	@Test
	public void validate_shouldAcceptValidLogicCriteria() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(2);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("logic"));
		Assert.assertTrue(flag.validate().getResult());
	}
	
	
	/* This won't be working until we get a better validator in place for logic evaluator */
	/** @Test
	public void validate_shouldRejectInvalidLogicCriteria() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(3);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("logic"));
		Assert.assertFalse(flag.validate().getResult());
	} **/
	
	@Test
	public void validate_shouldAcceptValidSQLCriteria() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(4);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("sql"));
		Assert.assertTrue(flag.validate().getResult());
	}
	
	@Test
	public void validate_shouldRejectInvalidSQLCriteria() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(5);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("sql"));
		Assert.assertFalse(flag.validate().getResult());
	}
	
	@Test
	public void validate_shouldAcceptValidGroovyCriteria() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(6);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("groovy"));
		Assert.assertTrue(flag.validate().getResult());
	}
	
	@Test
	public void validate_shouldRejectInvalidGroovyCriteria() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(7);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("groovy"));
		Assert.assertFalse(flag.validate().getResult());
	}
	
	/**
	 * eval(Patient) method tests
	 */
	
	// TODO add tests that return true; 
	// TODO test what eval does if invalid evaluator
	
	@Test
	public void eval_shouldReturnNullIfNoEvaluator() throws Exception{
		Patient patient = Context.getPatientService().getPatient(2);
		Flag flag = Context.getService(FlagService.class).getFlag(2);
		Assert.assertNull(flag.eval(patient));
	}
	
	@Test
	public void eval_logicShouldReturnNullIfNoPatient() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(2);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("logic"));
		Assert.assertNull(flag.eval(null));
	}
	
	@Test
	public void eval_sqlShouldReturnNullIfNoPatient() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(4);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("sql"));
		Assert.assertNull(flag.eval(null));
	}
	
	@Test
	public void eval_groovyShouldReturnNullIfNoPatient() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(6);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("groovy"));
		Assert.assertNull(flag.eval(null));
	}
	
	@Test
	public void eval_logicShouldReturnFalseForTestPatient() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(2);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("logic"));
		Assert.assertFalse(flag.eval(Context.getPatientService().getPatient(2)));
	}
	
	@Test
	public void eval_sqlShouldReturnFalseForTestPatient() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(4);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("sql"));
		Assert.assertFalse(flag.eval(Context.getPatientService().getPatient(2)));
	}
	
	@Test
	public void eval_groovyShouldReturnFalseForTestPatient() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(6);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("groovy"));
		Assert.assertFalse(flag.eval(Context.getPatientService().getPatient(2)));
	}

	/**
	 * evalCohort(Cohort) method tests
	 */
	
	// TODO add tests that actually create non-empty result sets
	// TODO add tests to verify what evalCohort does if invalid evaluator
	
	@Test
	public void evalCohort_shouldReturnNullIfNoEvaluator() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(2);
		Assert.assertNull(flag.evalCohort(new Cohort()));
	}
	
	@Test
	public void evalCohort_logicShouldReturnEmptyCohort() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(2);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("logic"));
		Cohort cohort = flag.evalCohort(new Cohort());
		Assert.assertTrue(cohort.isEmpty());
	}
	
	@Test
	public void evalCohort_sqlShouldReturnEmptyCohort() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(4);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("sql"));
		Cohort cohort = flag.evalCohort(new Cohort());
		Assert.assertTrue(cohort.isEmpty());
	}
	
	@Test
	public void evalCohort_logicNullShouldReturnEmptyCohort() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(2);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("logic"));
		Cohort cohort = flag.evalCohort(null);
		Assert.assertTrue(cohort.isEmpty());
	}
	
	@Test
	public void evalCohort_sqlNullShouldReturnCohort() throws Exception{
		Flag flag = Context.getService(FlagService.class).getFlag(4);
		flag.setEvaluator(PatientFlagsConstants.FLAG_EVALUATOR_MAP.get("sql"));
		Cohort cohort = flag.evalCohort(null);
		Assert.assertTrue(cohort.isEmpty());
	}
	
}